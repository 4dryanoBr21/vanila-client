package com.vanilaclient;

import com.vanilaclient.feature.DynamicFov;
import com.vanilaclient.feature.FullBright;
import com.vanilaclient.feature.OldAnimations;
import com.vanilaclient.feature.Perspective;
import com.vanilaclient.feature.ToggleSprint;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

@Mod(modid = VanilaClient.MODID, useMetadata = true, clientSideOnly = true,
        guiFactory = "com.vanilaclient.config.GuiFactory")
public class VanilaClient {
    public static final String MODID = "vanilaclient";
    private static final String KEY_CATEGORY = "Vanila Client";

    public static final KeyBinding KEY_TOGGLE_SPRINT = new KeyBinding("Alternar corrida", Keyboard.KEY_R, KEY_CATEGORY);
    public static final KeyBinding KEY_PERSPECTIVE = new KeyBinding("Perspectiva (segurar)", Keyboard.KEY_LMENU, KEY_CATEGORY);
    public static final KeyBinding KEY_FULL_BRIGHT = new KeyBinding("Full-bright", Keyboard.KEY_G, KEY_CATEGORY);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Config.load(event.getSuggestedConfigurationFile());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ClientRegistry.registerKeyBinding(KEY_TOGGLE_SPRINT);
        ClientRegistry.registerKeyBinding(KEY_PERSPECTIVE);
        ClientRegistry.registerKeyBinding(KEY_FULL_BRIGHT);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;
        if (event.phase == TickEvent.Phase.START) {
            ToggleSprint.tick(mc);
            FullBright.tick(mc);
        } else {
            OldAnimations.tick(mc);
        }
    }

    // Por quadro, para a perspectiva responder sem esperar o próximo tick.
    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            Perspective.update(Minecraft.getMinecraft());
        }
    }

    @SubscribeEvent
    public void onFovUpdate(FOVUpdateEvent event) {
        if (!Config.dynamicFov) {
            event.newfov = DynamicFov.staticFov(event.entity);
        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (MODID.equals(event.modID)) {
            Config.sync();
        }
    }
}
