package com.vanilaclient.mixin;

import com.vanilaclient.Config;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** old-animation-1.7: a armadura fica vermelha junto com o corpo ao tomar dano. */
@Mixin(LayerArmorBase.class)
public abstract class MixinLayerArmorBase {
    @Inject(method = "shouldCombineTextures", at = @At("HEAD"), cancellable = true)
    private void vanilaclient$redArmor(CallbackInfoReturnable<Boolean> cir) {
        if (Config.oldAnimation17) {
            cir.setReturnValue(true);
        }
    }
}
