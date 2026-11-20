package com.vanilaclient.feature;

import com.vanilaclient.Config;
import com.vanilaclient.mixin.EntityLivingBaseAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MovingObjectPosition;

/**
 * Blockhit da 1.7: segurando o uso do item (defender, comer, arco) e o ataque mirando
 * num bloco, o braço balança. Só visual, nenhum pacote é enviado.
 * O resto das animações antigas fica em MixinItemRenderer e MixinLayerArmorBase.
 */
public final class OldAnimations {
    private OldAnimations() {
    }

    public static void tick(Minecraft mc) {
        if (!Config.oldAnimation17) return;
        EntityPlayerSP player = mc.thePlayer;
        MovingObjectPosition hit = mc.objectMouseOver;
        if (!player.isUsingItem() || !mc.gameSettings.keyBindAttack.isKeyDown()
                || hit == null || hit.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
            return;
        }
        int swingEnd = ((EntityLivingBaseAccessor) player).invokeGetArmSwingAnimationEnd();
        if (!player.isSwingInProgress || player.swingProgressInt >= swingEnd / 2 || player.swingProgressInt < 0) {
            player.swingProgressInt = -1;
            player.isSwingInProgress = true;
        }
    }
}
