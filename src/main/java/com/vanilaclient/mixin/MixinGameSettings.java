package com.vanilaclient.mixin;

import com.vanilaclient.feature.FullBright;
import net.minecraft.client.settings.GameSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** full-bright: o options.txt guarda o brilho do jogador, não os 10000%. */
@Mixin(GameSettings.class)
public abstract class MixinGameSettings {
    @Inject(method = "saveOptions", at = @At("HEAD"))
    private void vanilaclient$saveRealGamma(CallbackInfo ci) {
        FullBright.beforeSave((GameSettings) (Object) this);
    }

    @Inject(method = "saveOptions", at = @At("RETURN"))
    private void vanilaclient$restoreFullBright(CallbackInfo ci) {
        FullBright.afterSave((GameSettings) (Object) this);
    }
}
