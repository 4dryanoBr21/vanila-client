package com.vanilaclient.mixin;

import com.vanilaclient.feature.TextBackground;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/** all-text-background-transparent */
@Mixin(GuiPlayerTabOverlay.class)
public abstract class MixinGuiPlayerTabOverlay {
    @Redirect(method = "renderPlayerlist", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiPlayerTabOverlay;drawRect(IIIII)V"))
    private void vanilaclient$skipBackground(int left, int top, int right, int bottom, int color) {
        TextBackground.drawRect(left, top, right, bottom, color);
    }
}
