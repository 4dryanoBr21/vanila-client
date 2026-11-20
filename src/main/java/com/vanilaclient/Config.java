package com.vanilaclient;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

/**
 * As opções do mod. Os campos são lidos direto pelos mixins em código quente,
 * por isso ficam como booleanos estáticos simples.
 */
public final class Config {
    public static final String CATEGORY = Configuration.CATEGORY_GENERAL;

    public static boolean toggleSprint = true;
    public static boolean perspective = true;
    public static boolean allTextBackgroundTransparent = true;
    public static boolean oldAnimation17 = true;
    public static boolean hitDelayFix = true;
    public static boolean dynamicFov = true;
    public static boolean textShadow = true;
    public static boolean fullBright = true;
    public static boolean inventoryCenter = true;
    public static boolean vanilaBadge = true;
    public static String badgeServer = "";

    public static Configuration config;

    private Config() {
    }

    static void load(File file) {
        config = new Configuration(file);
        sync();
    }

    static void sync() {
        toggleSprint = config.getBoolean("toggle-sprint", CATEGORY, true,
                "Tecla para deixar a corrida ligada sem segurar o sprint.");
        perspective = config.getBoolean("perspective", CATEGORY, true,
                "Segure a tecla para olhar em volta em terceira pessoa sem virar o personagem.");
        allTextBackgroundTransparent = config.getBoolean("all-text-background-transparent", CATEGORY, true,
                "Remove o fundo escuro do chat, nametags, scoreboard, tab e F3.");
        oldAnimation17 = config.getBoolean("old-animation-1.7", CATEGORY, true,
                "Animações da 1.7: blockhit, colocar blocos segurando o ataque, balanço do braço ao defender/comer/puxar o arco e armadura vermelha ao tomar dano.");
        hitDelayFix = config.getBoolean("hit-delay-fix", CATEGORY, true,
                "Remove o atraso de clique depois de errar um golpe.");
        dynamicFov = config.getBoolean("dynamic-fov", CATEGORY, true,
                "FOV muda ao correr, voar e com efeitos de velocidade/lentidão. Desligado, só o zoom do arco continua.");
        textShadow = config.getBoolean("text-shadow", CATEGORY, true,
                "Sombra nos textos do scoreboard, F3 e nametags, para continuarem legíveis sem o fundo escuro.");
        fullBright = config.getBoolean("full-bright", CATEGORY, true,
                "Brilho no máximo: enxerga em cavernas e à noite sem tocha. A tecla liga/desliga.");
        inventoryCenter = config.getBoolean("inventory-center", CATEGORY, true,
                "Inventário sempre no centro da tela, mesmo com efeitos ativos (os efeitos ficam à esquerda).");
        vanilaBadge = config.getBoolean("vanila-badge", CATEGORY, true,
                "Mostra o V do Vanila Client no nametag e no tab de quem também usa o client.");
        badgeServer = config.getString("badge-server", CATEGORY, "",
                "Endereço do servidor que diz quem está usando o client (ex.: https://vanila-badge.exemplo.workers.dev). Vazio: só você aparece com o V.").trim();
        if (config.hasChanged()) {
            config.save();
        }
    }

    /** Usado pela tecla do full-bright: muda e já grava no arquivo. */
    public static void setFullBright(boolean value) {
        fullBright = value;
        config.get(CATEGORY, "full-bright", true).set(value);
        config.save();
    }
}
