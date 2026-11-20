package com.vanilaclient.mixin;

import com.vanilaclient.feature.TextBackground;
import net.minecraft.client.gui.GuiChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/** all-text-background-transparent */
@Mixin(GuiChat.class)
public abstract class MixinGuiChat {
    @Redirect(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiChat;drawRect(IIIII)V"))
    private void vanilaclient$skipBackground(int left, int top, int right, int bottom, int color) {
        TextBackground.drawRect(left, top, right, bottom, color);
    }
}
