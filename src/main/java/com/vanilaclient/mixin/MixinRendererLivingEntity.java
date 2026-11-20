package com.vanilaclient.mixin;

import com.vanilaclient.Config;
import com.vanilaclient.feature.ClientBadge;
import com.vanilaclient.feature.TextShadow;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** all-text-background-transparent: fundo do nametag de quem está agachado. text-shadow: sombra no texto. vanila-badge: o V. */
@Mixin(RendererLivingEntity.class)
public abstract class MixinRendererLivingEntity {
    @Inject(method = "renderName(Lnet/minecraft/entity/EntityLivingBase;DDD)V", at = @At("HEAD"))
    private void vanilaclient$labelEntity(EntityLivingBase entity, double x, double y, double z, CallbackInfo ci) {
        ClientBadge.beginLabel(entity);
    }

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
        ClientBadge.drawLabelBadge(text, x, y, color);
        return TextShadow.drawString(font, text, x, y, color);
    }
}
