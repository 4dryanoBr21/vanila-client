package com.vanilaclient.mixin;

import com.vanilaclient.feature.TextBackground;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/** all-text-background-transparent */
@Mixin(GuiNewChat.class)
public abstract class MixinGuiNewChat {
    @Redirect(method = "drawChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiNewChat;drawRect(IIIII)V", ordinal = 0))
    private void vanilaclient$skipBackground(int left, int top, int right, int bottom, int color) {
        TextBackground.drawRect(left, top, right, bottom, color);
    }
}
