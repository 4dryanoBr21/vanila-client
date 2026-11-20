package com.vanilaclient.mixin;

import com.vanilaclient.feature.Perspective;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** perspective: com a tecla segurada, o mouse gira a câmera em vez do jogador. */
@Mixin(Entity.class)
public abstract class MixinEntity {
    @Inject(method = "setAngles", at = @At("HEAD"), cancellable = true)
    private void vanilaclient$rotateCamera(float yaw, float pitch, CallbackInfo ci) {
        if (Perspective.active && (Object) this instanceof EntityPlayerSP) {
            Perspective.rotate(yaw, pitch);
            ci.cancel();
        }
    }
}
