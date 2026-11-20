package com.vanilaclient.mixin;

import com.vanilaclient.feature.TextBackground;
import com.vanilaclient.feature.TextShadow;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiIngame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/** all-text-background-transparent e text-shadow */
@Mixin(GuiIngame.class)
public abstract class MixinGuiIngame {
    @Redirect(method = "renderScoreboard", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiIngame;drawRect(IIIII)V"))
    private void vanilaclient$skipBackground(int left, int top, int right, int bottom, int color) {
        TextBackground.drawRect(left, top, right, bottom, color);
    }

    @Redirect(method = "renderScoreboard", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawString(Ljava/lang/String;III)I"))
    private int vanilaclient$textShadow(FontRenderer font, String text, int x, int y, int color) {
        return TextShadow.drawString(font, text, x, y, color);
    }
}
