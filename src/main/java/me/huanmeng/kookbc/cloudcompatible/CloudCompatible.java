package me.huanmeng.kookbc.cloudcompatible;

import snw.jkook.plugin.BasePlugin;

public class CloudCompatible extends BasePlugin {
    @Override
    public void onEnable() {
        getLogger().info("Enabled");
        getLogger().info("Current CommandManager Impl: {}", getCore().getCommandManager());
    }
}
