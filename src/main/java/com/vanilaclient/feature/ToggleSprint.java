package com.vanilaclient.feature;

import com.vanilaclient.Config;
import com.vanilaclient.VanilaClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 * Mantém a tecla de sprint do jogo "pressionada" enquanto estiver ligado, assim as
 * regras da corrida (fome, colisão, agachar) continuam sendo as do próprio Minecraft.
 */
public final class ToggleSprint {
    private static boolean toggled;

    private ToggleSprint() {
    }

    public static void tick(Minecraft mc) {
        while (VanilaClient.KEY_TOGGLE_SPRINT.isPressed()) {
            if (Config.toggleSprint) {
                setToggled(mc, !toggled);
            }
        }
        if (!Config.toggleSprint) {
            if (toggled) setToggled(mc, false);
            return;
        }
        if (toggled && mc.currentScreen == null) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
        }
    }

    private static void setToggled(Minecraft mc, boolean value) {
        toggled = value;
        if (!value) {
            // Devolve a tecla ao estado físico real.
            int code = mc.gameSettings.keyBindSprint.getKeyCode();
            KeyBinding.setKeyBindState(code, isPhysicallyDown(code));
        }
    }

    private static boolean isPhysicallyDown(int code) {
        if (code == 0) return false;
        return code < 0 ? Mouse.isButtonDown(code + 100) : Keyboard.isKeyDown(code);
    }
}
