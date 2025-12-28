package me.ybbbno.noelytraworlds;

import me.deadybbb.ybmj.BasicConfigHandler;
import me.deadybbb.ybmj.PluginProvider;

import java.util.HashSet;
import java.util.Set;

public class WorldsConfigHandler extends BasicConfigHandler {

    public WorldsConfigHandler(PluginProvider plugin) {
        super(plugin, "worlds.yml");
    }

    public Set<String> getWorlds() {
        reloadConfig();
        return new HashSet<>(config.getStringList("worlds"));
    }

    public void setWorlds(Set<String> world) {
        config.set("worlds", world.stream().toList());
        saveConfig();
    }
}
