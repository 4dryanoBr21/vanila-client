package com.vanilaclient.feature;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;

/**
 * dynamic-fov desligado: o FOV para de mudar com corrida, voo e efeitos de poção.
 * Usa o FOVUpdateEvent do Forge (que o OptiFine também dispara), então não mexe no
 * zoom do OptiFine nem precisa de mixin.
 */
public final class DynamicFov {
    private DynamicFov() {
    }

    /** Mesmo zoom do arco de AbstractClientPlayer.getFovModifier, sem o resto. */
    public static float staticFov(EntityPlayer player) {
        if (!player.isUsingItem() || player.getItemInUse().getItem() != Items.bow) {
            return 1.0F;
        }
        float charge = player.getItemInUseDuration() / 20.0F;
        charge = charge > 1.0F ? 1.0F : charge * charge;
        return 1.0F - charge * 0.15F;
    }
}
