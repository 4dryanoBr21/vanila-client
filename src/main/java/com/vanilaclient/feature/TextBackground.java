package com.vanilaclient.feature;

import com.vanilaclient.Config;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

/**
 * all-text-background-transparent: no lugar de Gui.drawRect. Pular o retângulo não basta,
 * porque ele deixa o blend desligado ao terminar e o texto desenhado depois conta com isso:
 * o scoreboard usa cor com alfa 0x20 e, com o blend que sobrou de outro elemento da HUD
 * ligado, ficava quase invisível em alguns quadros (o "pisca").
 */
public final class TextBackground {
    private TextBackground() {
    }

    public static void drawRect(int left, int top, int right, int bottom, int color) {
        if (Config.allTextBackgroundTransparent) {
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
        } else {
            Gui.drawRect(left, top, right, bottom, color);
        }
    }
}
