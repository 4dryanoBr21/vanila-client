package com.vanilaclient.mixin;

import com.vanilaclient.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
    @Shadow
    private int leftClickCounter;

    /** hit-delay-fix: errar um golpe deixa de travar os próximos cliques por 10 ticks. */
    @Inject(method = "clickMouse", at = @At("HEAD"))
    private void vanilaclient$resetHitDelay(CallbackInfo ci) {
        if (Config.hitDelayFix) {
            leftClickCounter = 0;
        }
    }

    /**
     * old-animation-1.7: a 1.8 ignora o clique direito enquanto o ataque está quebrando um bloco,
     * então segurando o botão de bater não dava para colocar blocos. Na 1.7 dava.
     */
    @Redirect(method = "rightClickMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;getIsHittingBlock()Z"))
    private boolean vanilaclient$useWhileHitting(PlayerControllerMP controller) {
        return !Config.oldAnimation17 && controller.getIsHittingBlock();
    }
}
