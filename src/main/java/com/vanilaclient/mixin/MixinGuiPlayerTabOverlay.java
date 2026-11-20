package com.vanilaclient.mixin;

import com.vanilaclient.feature.ClientBadge;
import com.vanilaclient.feature.TextBackground;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/** all-text-background-transparent e vanila-badge (o V antes do nome). */
@Mixin(GuiPlayerTabOverlay.class)
public abstract class MixinGuiPlayerTabOverlay {
    @Redirect(method = "renderPlayerlist", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiPlayerTabOverlay;drawRect(IIIII)V"))
    private void vanilaclient$skipBackground(int left, int top, int right, int bottom, int color) {
        TextBackground.drawRect(left, top, right, bottom, color);
    }

    // O nome ganha um espaço na frente tanto ao medir a largura da coluna quanto ao desenhar.
    @Redirect(method = "renderPlayerlist", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiPlayerTabOverlay;getPlayerName(Lnet/minecraft/client/network/NetworkPlayerInfo;)Ljava/lang/String;"))
    private String vanilaclient$badgeSpace(GuiPlayerTabOverlay tab, NetworkPlayerInfo info) {
        return ClientBadge.tabName(info, tab.getPlayerName(info));
    }

    @Redirect(method = "renderPlayerlist", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawStringWithShadow(Ljava/lang/String;FFI)I"))
    private int vanilaclient$drawBadge(FontRenderer font, String text, float x, float y, int color) {
        return font.drawStringWithShadow(ClientBadge.drawTabBadge(text, x, y), x, y, color);
    }
}
