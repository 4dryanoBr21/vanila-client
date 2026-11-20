package com.vanilaclient.feature;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.vanilaclient.Config;
import com.vanilaclient.VanilaClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * vanila-badge: o "V" no nametag e no tab de quem também usa o Vanila Client.
 * A cada minuto o client avisa o servidor (badge-server) que está online e pergunta quais
 * jogadores do tab também estão. Sem servidor configurado, só o próprio jogador ganha o V.
 */
public final class ClientBadge {
    private static final ResourceLocation TEXTURE = new ResourceLocation(VanilaClient.MODID, "textures/badge.png");
    private static final int SIZE = 8;
    private static final int GAP = 2;
    private static final int SYNC_TICKS = 20 * 60;
    private static final int MAX_PLAYERS = 200;

    /** Marcador no início do nome do tab: "§v" não é um código de formatação e não aparece nem ocupa largura. */
    private static final String TAB_MARKER = "§v";
    /** Espaço reservado no tab para o V (três espaços de 4 px). */
    private static final String TAB_PADDING = "   ";

    private static final Set<UUID> users = Collections.newSetFromMap(new ConcurrentHashMap<UUID, Boolean>());
    private static final AtomicBoolean syncing = new AtomicBoolean();
    private static final ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
        Thread thread = new Thread(r, "Vanila Client badge");
        thread.setDaemon(true);
        return thread;
    });

    private static int ticks;
    private static NetHandlerPlayClient lastConnection;

    /** Entidade do nametag sendo desenhado agora (renderLivingLabel e renderName). */
    private static Entity labelEntity;

    private ClientBadge() {
    }

    public static void tick(Minecraft mc) {
        NetHandlerPlayClient connection = mc.getNetHandler();
        if (connection != lastConnection) {
            // Trocou de servidor: a lista anterior não vale mais, e sincroniza logo.
            lastConnection = connection;
            users.clear();
            ticks = SYNC_TICKS - 40;
        }
        if (!Config.vanilaBadge || connection == null || Config.badgeServer.isEmpty()) return;
        if (++ticks < SYNC_TICKS || !syncing.compareAndSet(false, true)) return;
        ticks = 0;

        JsonObject body = new JsonObject();
        body.addProperty("uuid", mc.thePlayer.getUniqueID().toString());
        JsonArray players = new JsonArray();
        for (NetworkPlayerInfo info : connection.getPlayerInfoMap()) {
            if (players.size() >= MAX_PLAYERS) break;
            UUID id = info.getGameProfile().getId();
            if (id != null) players.add(new JsonPrimitive(id.toString()));
        }
        body.add("players", players);
        String url = Config.badgeServer.replaceAll("/+$", "") + "/sync";
        executor.execute(() -> {
            try {
                sync(url, body.toString());
            } catch (Exception e) {
                // Servidor fora do ar não pode atrapalhar o jogo; tenta de novo no próximo ciclo.
            } finally {
                syncing.set(false);
            }
        });
    }

    private static void sync(String url, String body) throws Exception {
        HttpURLConnection http = (HttpURLConnection) new URL(url).openConnection();
        http.setConnectTimeout(5000);
        http.setReadTimeout(5000);
        http.setRequestMethod("POST");
        http.setRequestProperty("Content-Type", "application/json");
        http.setRequestProperty("User-Agent", "VanilaClient");
        http.setDoOutput(true);
        try (OutputStream out = http.getOutputStream()) {
            out.write(body.getBytes(StandardCharsets.UTF_8));
        }
        if (http.getResponseCode() != 200) return;
        try (Reader reader = new InputStreamReader(http.getInputStream(), StandardCharsets.UTF_8)) {
            JsonArray online = new JsonParser().parse(reader).getAsJsonObject().getAsJsonArray("users");
            Set<UUID> found = Collections.newSetFromMap(new ConcurrentHashMap<UUID, Boolean>());
            for (JsonElement element : online) {
                found.add(UUID.fromString(element.getAsString()));
            }
            users.retainAll(found);
            users.addAll(found);
        }
    }

    public static boolean isUser(UUID id) {
        if (!Config.vanilaBadge || id == null) return false;
        Minecraft mc = Minecraft.getMinecraft();
        return users.contains(id) || (mc.thePlayer != null && id.equals(mc.thePlayer.getUniqueID()));
    }

    // Nametag

    public static void beginLabel(Entity entity) {
        labelEntity = entity;
    }

    /** Desenha o V à esquerda do nome, se o texto for o nome do jogador (e não o placar abaixo dele). */
    public static void drawLabelBadge(String text, int x, int y, int color) {
        Entity entity = labelEntity;
        if (!(entity instanceof EntityPlayer) || !isUser(entity.getUniqueID())
                || !text.equals(entity.getDisplayName().getFormattedText())) {
            return;
        }
        float alpha = (color >>> 24) == 0 ? 1F : (color >>> 24) / 255F;
        draw(x - SIZE - GAP, y, alpha);
    }

    // Tab

    public static String tabName(NetworkPlayerInfo info, String name) {
        return isUser(info.getGameProfile().getId()) ? TAB_MARKER + TAB_PADDING + name : name;
    }

    /** Tira o marcador do nome do tab e desenha o V no espaço reservado. Devolve o texto a escrever. */
    public static String drawTabBadge(String text, float x, float y) {
        if (!text.startsWith(TAB_MARKER)) return text;
        draw(Math.round(x), Math.round(y), 1F);
        return text.substring(TAB_MARKER.length());
    }

    private static void draw(int x, int y, float alpha) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
        // A textura é maior que o ícone na tela: filtro linear para o V não ficar serrilhado.
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        // O tab e o scoreboard dependem do blend como estava (ver TextBackground): volta ao final.
        boolean blend = GL11.glIsEnabled(GL11.GL_BLEND);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(1F, 1F, 1F, alpha);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer renderer = tessellator.getWorldRenderer();
        renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        renderer.pos(x, y + SIZE, 0).tex(0, 1).endVertex();
        renderer.pos(x + SIZE, y + SIZE, 0).tex(1, 1).endVertex();
        renderer.pos(x + SIZE, y, 0).tex(1, 0).endVertex();
        renderer.pos(x, y, 0).tex(0, 0).endVertex();
        tessellator.draw();
        GlStateManager.color(1F, 1F, 1F, 1F);
        if (!blend) GlStateManager.disableBlend();
    }
}
