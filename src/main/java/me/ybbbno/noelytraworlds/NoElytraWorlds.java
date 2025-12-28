package me.ybbbno.noelytraworlds;

import me.deadybbb.ybmj.PluginProvider;
import me.ybbbno.noelytraworlds.commands.WorldsCommand;

public final class NoElytraWorlds extends PluginProvider {
    public WorldsHandler handler;

    @Override
    public void onEnable() {
        handler = new WorldsHandler(this);
        getServer().getPluginManager().registerEvents(handler, this);
        handler.init();

        registerCommand("noelytraworld", new WorldsCommand(this));
    }

    @Override
    public void onDisable() {
        handler.deinit();
    }
}
