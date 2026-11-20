package com.vanilaclient.mixin;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Blocos que às vezes não são colocados: na 1.8 o raio da mira do jogador sai de rotationYawHead,
 * que só muda no tick e fica até um tick atrasado em relação à câmera. Ao virar rápido para o chão
 * e clicar, o raio ainda apontava para onde você olhava antes. O jogador local volta a usar a
 * rotação de Entity.getLook, a mesma da câmera (como na 1.7).
 */
@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase extends Entity {
    public MixinEntityLivingBase(World worldIn) {
        super(worldIn);
    }

    @Inject(method = "getLook", at = @At("HEAD"), cancellable = true)
    private void vanilaclient$mouseDelayFix(float partialTicks, CallbackInfoReturnable<Vec3> cir) {
        if ((Object) this instanceof EntityPlayerSP) {
            cir.setReturnValue(super.getLook(partialTicks));
        }
    }
}
