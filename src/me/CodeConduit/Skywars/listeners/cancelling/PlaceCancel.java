package me.CodeConduit.Skywars.listeners.cancelling;

import me.CodeConduit.Skywars.Main;
import me.CodeConduit.Skywars.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.io.IOException;
import java.util.Objects;

public class PlaceCancel implements Listener {
    //Variables
    private final Main plugin;
    //Constructor
    public PlaceCancel(Main plugin) {
        this.plugin = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    //Event Handler
    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        Location location = e.getBlockPlaced().getLocation();

        if (Objects.equals(plugin.getDataConfig().get("players." + player.getUniqueId() + ".inGuiMode"), true)) {
            e.setCancelled(true);
            if (e.getBlockPlaced().getType().equals(Material.DEAD_FIRE_CORAL_BLOCK)) {
                e.setCancelled(false);
                //Do yaml configuration
                plugin.getDataConfig().set("arenas." + plugin.getPlayerArena(player) + ".boxSpawns." + plugin.getDataConfig().get("arenas." + plugin.getPlayerArena(player) + ".boxSpawnCount"), location);
                plugin.getDataConfig().set("arenas." + plugin.getPlayerArena(player) + ".boxSpawnCount", plugin.getDataConfig().getInt("arenas." + plugin.getPlayerArena(player) + ".boxSpawnCount") + 1);
                player.sendMessage(Utils.chat("&6Placed spawn point at&e " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ()));
            } else if (e.getBlockPlaced().getType().equals(Material.CHEST)) {
                e.setCancelled(false);
                //Do yaml configuration
                plugin.getDataConfig().set("arenas." + plugin.getPlayerArena(player) + ".islandChests." + plugin.getDataConfig().get("arenas." + plugin.getPlayerArena(player) + ".islandChestCount"), location);
                plugin.getDataConfig().set("arenas." + plugin.getPlayerArena(player) + ".islandChestCount", plugin.getDataConfig().getInt("arenas." + plugin.getPlayerArena(player) + ".islandChestCount") + 1);
                player.sendMessage(Utils.chat("&6Placed island chest point at&e " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ()));
            } else if (e.getBlockPlaced().getType().equals(Material.TRAPPED_CHEST)) {
                e.setCancelled(false);
                //Do yaml configuration
                plugin.getDataConfig().set("arenas." + plugin.getPlayerArena(player) + ".middleChests." + plugin.getDataConfig().get("arenas." + plugin.getPlayerArena(player) + ".middleChestCount"), location);
                plugin.getDataConfig().set("arenas." + plugin.getPlayerArena(player) + ".middleChestCount", plugin.getDataConfig().getInt("arenas." + plugin.getPlayerArena(player) + ".middleChestCount") + 1);
                player.sendMessage(Utils.chat("&6Placed middle chest point at&e " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ()));
            }
        }
        //Save
        try {
            plugin.getDataConfig().save(plugin.getDataFile());
        } catch (IOException error) {
            error.printStackTrace();
        }
    }
}
