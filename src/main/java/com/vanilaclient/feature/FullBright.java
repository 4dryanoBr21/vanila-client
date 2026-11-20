package com.vanilaclient.feature;

import com.vanilaclient.Config;
import com.vanilaclient.VanilaClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;

/**
 * full-bright: sobe o brilho (gamma) para 10000% enquanto estiver ligado. Mexer no gamma em
 * vez do lightmap funciona igual com o OptiFine. O brilho escolhido pelo jogador fica guardado
 * e é ele que vai para o options.txt (MixinGameSettings).
 */
public final class FullBright {
    private static final float GAMMA = 100.0F;
    private static boolean applied;
    private static float savedGamma;

    private FullBright() {
    }

    public static void tick(Minecraft mc) {
        while (VanilaClient.KEY_FULL_BRIGHT.isPressed()) {
            Config.setFullBright(!Config.fullBright);
        }
        GameSettings settings = mc.gameSettings;
        if (Config.fullBright) {
            if (!applied || settings.gammaSetting != GAMMA) {
                // Primeira vez, ou o jogador mexeu no slider de brilho: esse é o valor real dele.
                savedGamma = settings.gammaSetting;
                applied = true;
            }
            settings.gammaSetting = GAMMA;
        } else if (applied) {
            settings.gammaSetting = savedGamma;
            applied = false;
        }
    }

    public static void beforeSave(GameSettings settings) {
        if (applied) settings.gammaSetting = savedGamma;
    }

    public static void afterSave(GameSettings settings) {
        if (applied) settings.gammaSetting = GAMMA;
    }
}
