package com.vanilaclient.feature;

import com.vanilaclient.Config;
import net.minecraft.client.gui.FontRenderer;

/**
 * text-shadow: troca drawString por drawStringWithShadow nos textos que o jogo desenha
 * sem sombra (scoreboard, F3 e nametags). Chat, caixa de digitar e tab já têm sombra.
 */
public final class TextShadow {
    private TextShadow() {
    }

    public static int drawString(FontRenderer font, String text, int x, int y, int color) {
        return Config.textShadow
                ? font.drawStringWithShadow(text, x, y, color)
                : font.drawString(text, x, y, color);
    }
}
