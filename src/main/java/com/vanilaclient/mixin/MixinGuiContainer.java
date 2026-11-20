package com.vanilaclient.mixin;

import net.minecraft.client.gui.inventory.GuiContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Teclas da hotbar ligadas a botões do mouse (os laterais, por exemplo) também trocam o item
 * do slot sob o cursor no inventário. O jogo só confere essas teclas no teclado (keyTyped).
 */
@Mixin(GuiContainer.class)
public abstract class MixinGuiContainer {
    @Shadow
    protected abstract boolean checkHotbarKeys(int keyCode);

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void vanilaclient$hotbarMouseButtons(int mouseX, int mouseY, int mouseButton, CallbackInfo ci) {
        // Botões do mouse aparecem nas teclas como botão - 100. Esquerdo e direito ficam com o inventário.
        if (mouseButton > 1 && checkHotbarKeys(mouseButton - 100)) {
            ci.cancel();
        }
    }
}
