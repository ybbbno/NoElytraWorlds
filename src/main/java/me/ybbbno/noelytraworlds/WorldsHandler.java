package me.ybbbno.noelytraworlds;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import me.deadybbb.ybmj.BasicManagerHandler;
import me.deadybbb.ybmj.LegacyTextHandler;
import me.deadybbb.ybmj.PluginProvider;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class WorldsHandler extends BasicManagerHandler implements Listener {

    private final Set<String> worlds = new HashSet<>();

    private final WorldsConfigHandler config;

    public WorldsHandler(PluginProvider plugin) {
        super(plugin);
        this.config = new WorldsConfigHandler(plugin);
    }

    @Override
    protected void onInit() {
        worlds.clear();
        worlds.addAll(config.getWorlds());
    }

    @Override
    protected void onDeinit() {
        config.setWorlds(worlds);
    }

    public boolean isWorldAllowed(String world) {
        return !worlds.contains(world);
    }

    public boolean addWorld(String world) {
        if (Bukkit.getWorld(world) != null) {
            return worlds.add(world);
        }
        return false;
    }

    public boolean removeWorld(String world) {
        if (Bukkit.getWorld(world) != null) {
            return worlds.remove(world);
        }
        return false;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!is_init) return;
        if (isWorldAllowed(event.getPlayer().getWorld().getName())) return;

        Action action = event.getAction();
        if (action.isLeftClick() || !action.isRightClick()) return;
        if (!event.hasItem()) return;

        ItemStack item = event.getItem();
        if (item == null) return;

        if (item.getType() != Material.ELYTRA) return;

        event.setCancelled(true);
        event.getPlayer().sendActionBar(LegacyTextHandler.parseText("<red>В этом мире полёт запрещён!"));
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!is_init) return;

        Player player = event.getPlayer();
        if (isWorldAllowed(player.getWorld().getName())) return;

        if (!player.isGliding()) return;

        ItemStack item = player.getInventory().getItem(EquipmentSlot.CHEST);
        if (item.getType() != Material.ELYTRA) return;

        unequipElytraFromPlayer(player, item);
    }

    @EventHandler
    public void onPlayerArmorChangedEvent(PlayerArmorChangeEvent event) {
        if (!is_init) return;

        Player player = event.getPlayer();
        if (isWorldAllowed(player.getWorld().getName())) return;

        unequipElytraFromPlayer(player, event.getNewItem());
    }

    private void unequipElytraFromPlayer(Player player, ItemStack elytra) {
        if (elytra.getType() != Material.ELYTRA) return;
        player.getInventory().setItem(EquipmentSlot.CHEST, ItemStack.of(Material.AIR));
        if (!player.getInventory().addItem(elytra).isEmpty()) {
            player.dropItem(elytra);
        }
        player.sendActionBar(LegacyTextHandler.parseText("<red>В этом мире полёт запрещён!"));
    }
}
