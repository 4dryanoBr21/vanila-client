package com.vanilaclient.mixin;

import com.vanilaclient.Config;
import com.vanilaclient.feature.TextShadow;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/** all-text-background-transparent: fundo do nametag de quem está agachado. text-shadow: sombra no texto. */
@Mixin(RendererLivingEntity.class)
public abstract class MixinRendererLivingEntity {
    @Redirect(method = "renderName(Lnet/minecraft/entity/EntityLivingBase;DDD)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/Tessellator;draw()V"))
    private void vanilaclient$skipNametagBackground(Tessellator tessellator) {
        if (Config.allTextBackgroundTransparent) {
            // Descarta o quad já montado sem enviá-lo à GPU.
            WorldRenderer renderer = tessellator.getWorldRenderer();
            renderer.finishDrawing();
            renderer.reset();
        } else {
            tessellator.draw();
        }
    }

    @Redirect(method = "renderName(Lnet/minecraft/entity/EntityLivingBase;DDD)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawString(Ljava/lang/String;III)I"))
    private int vanilaclient$textShadow(FontRenderer font, String text, int x, int y, int color) {
        return TextShadow.drawString(font, text, x, y, color);
    }
}
