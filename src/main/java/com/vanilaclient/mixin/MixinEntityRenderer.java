package com.vanilaclient.mixin;

import com.vanilaclient.feature.Perspective;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/** perspective: a câmera usa os ângulos livres em vez da rotação do jogador. */
@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer {
    @Redirect(method = "orientCamera", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;rotationYaw:F", opcode = Opcodes.GETFIELD))
    private float vanilaclient$yaw(Entity entity) {
        return Perspective.active ? Perspective.cameraYaw : entity.rotationYaw;
    }

    @Redirect(method = "orientCamera", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;prevRotationYaw:F", opcode = Opcodes.GETFIELD))
    private float vanilaclient$prevYaw(Entity entity) {
        return Perspective.active ? Perspective.cameraYaw : entity.prevRotationYaw;
    }

    @Redirect(method = "orientCamera", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;rotationPitch:F", opcode = Opcodes.GETFIELD))
    private float vanilaclient$pitch(Entity entity) {
        return Perspective.active ? Perspective.cameraPitch : entity.rotationPitch;
    }

    @Redirect(method = "orientCamera", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;prevRotationPitch:F", opcode = Opcodes.GETFIELD))
    private float vanilaclient$prevPitch(Entity entity) {
        return Perspective.active ? Perspective.cameraPitch : entity.prevRotationPitch;
    }
}
