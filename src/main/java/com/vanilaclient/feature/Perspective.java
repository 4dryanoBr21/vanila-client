package com.vanilaclient.feature;

import com.vanilaclient.Config;
import com.vanilaclient.VanilaClient;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;

/**
 * Enquanto a tecla estiver segurada, a câmera gira em volta do jogador em terceira
 * pessoa e o personagem continua olhando para onde estava.
 * A rotação da câmera é aplicada em MixinEntityRenderer e o mouse é desviado em MixinEntity.
 */
public final class Perspective {
    public static boolean active;
    public static float cameraYaw;
    public static float cameraPitch;
    private static int previousView;

    private Perspective() {
    }

    public static void update(Minecraft mc) {
        boolean wanted = Config.perspective && mc.thePlayer != null && VanilaClient.KEY_PERSPECTIVE.isKeyDown();
        if (wanted == active) return;

        active = wanted;
        if (wanted) {
            cameraYaw = mc.thePlayer.rotationYaw;
            cameraPitch = mc.thePlayer.rotationPitch;
            previousView = mc.gameSettings.thirdPersonView;
            mc.gameSettings.thirdPersonView = 1;
        } else {
            mc.gameSettings.thirdPersonView = previousView;
        }
        if (mc.renderGlobal != null) {
            mc.renderGlobal.setDisplayListEntitiesDirty();
        }
    }

    /** Mesmo cálculo de Entity.setAngles, só que aplicado à câmera. */
    public static void rotate(float yaw, float pitch) {
        cameraYaw += yaw * 0.15F;
        cameraPitch = MathHelper.clamp_float(cameraPitch - pitch * 0.15F, -90.0F, 90.0F);
    }
}
