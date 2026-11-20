package com.vanilaclient.mixin;

import com.vanilaclient.Config;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** hit-delay-fix: errar um golpe deixa de travar os próximos cliques por 10 ticks. */
@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
    @Shadow
    private int leftClickCounter;

    @Inject(method = "clickMouse", at = @At("HEAD"))
    private void vanilaclient$resetHitDelay(CallbackInfo ci) {
        if (Config.hitDelayFix) {
            leftClickCounter = 0;
        }
    }
}
