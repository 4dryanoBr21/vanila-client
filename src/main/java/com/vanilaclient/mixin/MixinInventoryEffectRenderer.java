package com.vanilaclient.mixin;

import com.vanilaclient.Config;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.inventory.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * inventory-center: com efeitos ativos o jogo empurra o inventário para a direita para abrir
 * espaço à lista de efeitos. Aqui ele volta ao centro; os efeitos continuam à esquerda dele.
 */
@Mixin(InventoryEffectRenderer.class)
public abstract class MixinInventoryEffectRenderer extends GuiContainer {
    private MixinInventoryEffectRenderer(Container container) {
        super(container);
    }

    @Inject(method = "updateActivePotionEffects", at = @At("TAIL"))
    private void vanilaclient$centerInventory(CallbackInfo ci) {
        if (Config.inventoryCenter) {
            guiLeft = (width - xSize) / 2;
        }
    }
}
