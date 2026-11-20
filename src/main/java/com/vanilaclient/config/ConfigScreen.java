package com.vanilaclient.config;

import com.vanilaclient.Config;
import com.vanilaclient.VanilaClient;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;

public class ConfigScreen extends GuiConfig {
    public ConfigScreen(GuiScreen parent) {
        super(parent,
                new ConfigElement(Config.config.getCategory(Config.CATEGORY)).getChildElements(),
                VanilaClient.MODID, false, false, "Vanila Client");
    }
}
