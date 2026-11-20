package com.vanilaclient.mixin;

import com.vanilaclient.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * old-animation-1.7: na 1.8 o braço fica parado enquanto você defende, come ou puxa o arco
 * (transformFirstPersonItem recebe 0). Na 1.7 ele balança junto, que é o blockhit.
 */
@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {
    @Shadow
    @Final
    private Minecraft mc;

    private float vanilaclient$partialTicks;

    @Inject(method = "renderItemInFirstPerson", at = @At("HEAD"))
    private void vanilaclient$capturePartialTicks(float partialTicks, CallbackInfo ci) {
        vanilaclient$partialTicks = partialTicks;
    }

    @ModifyArg(method = "renderItemInFirstPerson", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemRenderer;transformFirstPersonItem(FF)V"), index = 1)
    private float vanilaclient$swingWhileUsing(float swingProgress) {
        return Config.oldAnimation17 ? mc.thePlayer.getSwingProgress(vanilaclient$partialTicks) : swingProgress;
    }
}
