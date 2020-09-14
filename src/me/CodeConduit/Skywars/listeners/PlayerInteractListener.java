package me.CodeConduit.Skywars.listeners;

import me.CodeConduit.Skywars.Main;
import me.CodeConduit.Skywars.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

public class PlayerInteractListener implements Listener {
    //Variables
    private final Main plugin;
    //Constructor
    public PlayerInteractListener(Main plugin) {
        this.plugin = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    //Event handler
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Location targetLoc = player.getTargetBlock((Set<Material>) null, 100).getLocation();
        Location dummyLocation;

        //Abort Block
        try {
            if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                if (player.getInventory().getItemInHand().equals(Main.abort)) {
                    //Delete placed blocks
                    for (int i = 0; i < plugin.getDataConfig().getInt("arenas." + plugin.getPlayerArena(player) + ".boxSpawnCount"); i++) {
                        Objects.requireNonNull(plugin.getDataConfig().getLocation("arenas." + plugin.getPlayerArena(player) + ".boxSpawns." + i)).getBlock().setType(Material.AIR);
                    }
                    for (int i = 0; i < plugin.getDataConfig().getInt("arenas." + plugin.getPlayerArena(player) + ".islandChestCount"); i++) {
                        Objects.requireNonNull(plugin.getDataConfig().getLocation("arenas." + plugin.getPlayerArena(player) + ".islandChests." + i)).getBlock().setType(Material.AIR);
                    }
                    for (int i = 0; i < plugin.getDataConfig().getInt("arenas." + plugin.getPlayerArena(player) + ".middleChestCount"); i++) {
                        Objects.requireNonNull(plugin.getDataConfig().getLocation("arenas." + plugin.getPlayerArena(player) + ".middleChests." + i)).getBlock().setType(Material.AIR);
                    }
                    //Take out of gui mode and delete arena file
                    plugin.getDataConfig().set("players." + player.getUniqueId() + ".inGuiMode", false);
                    plugin.getDataConfig().set("arenas." + plugin.getPlayerArena(player), null);
                    player.getInventory().clear();
                    //Notify player
                    player.sendMessage(Utils.chat("&6Skywars arena creation aborted."));
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                }
            }
        } catch (NullPointerException error) {
            error.printStackTrace();
        }

        //Duo/solo
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            if (player.getInventory().getItemInHand().equals(Main.soloMode)) {
                plugin.getDataConfig().set("arenas." + plugin.getPlayerArena(player) + ".isSolo", false);
                player.getInventory().setItem(1, null);
                player.getInventory().setItem(1, Main.duoMode);
            } else if (player.getInventory().getItemInHand().equals(Main.duoMode)) {
                plugin.getDataConfig().set("arenas." + plugin.getPlayerArena(player) + ".isSolo", true);
                player.getInventory().setItem(1, null);
                player.getInventory().setItem(1, Main.soloMode);
            }
        }

        //Location Targeting
        if (player.getInventory().getItemInHand().equals(Main.spawnSelect)) {
            if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                //Save values to yml
                plugin.getDataConfig().set("arenas." + plugin.getPlayerArena(player) + ".spawnLoc2.X", targetLoc.getBlockX());
                plugin.getDataConfig().set("arenas." + plugin.getPlayerArena(player) + ".spawnLoc2.Y", targetLoc.getBlockY());
                plugin.getDataConfig().set("arenas." + plugin.getPlayerArena(player) + ".spawnLoc2.Z", targetLoc.getBlockZ());
                //Notify
                player.sendMessage(Utils.chat("&6Second location set to:&e " + targetLoc.getBlockX() + ", " + targetLoc.getBlockY() + ", " + targetLoc.getBlockZ()));
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            } else if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                //Save values to yml
                plugin.getDataConfig().set("arenas." + plugin.getPlayerArena(player) + ".spawnLoc1.X", targetLoc.getBlockX());
                plugin.getDataConfig().set("arenas." + plugin.getPlayerArena(player) + ".spawnLoc1.Y", targetLoc.getBlockY());
                plugin.getDataConfig().set("arenas." + plugin.getPlayerArena(player) + ".spawnLoc1.Z", targetLoc.getBlockZ());
                //Notify
                player.sendMessage(Utils.chat("&6First location set to:&e " + targetLoc.getBlockX() + ", " + targetLoc.getBlockY() + ", " + targetLoc.getBlockZ()));
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            }
        }

        //Breaking Box Spawns
        if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            if (e.getClickedBlock().getType().equals(Material.DEAD_FIRE_CORAL_BLOCK)) {
                //Remove data for block
                for (int i = 0; i < plugin.getDataConfig().getInt("arenas." + plugin.getPlayerArena(player) + ".boxSpawnCount"); i++) {
                    e.getClickedBlock().setType(Material.AIR);
                    plugin.getDataConfig().set("arenas." + plugin.getPlayerArena(player) + ".boxSpawnCount", plugin.getDataConfig().getInt("arenas." + plugin.getPlayerArena(player) + ".boxSpawnCount") - 1);
                    plugin.getDataConfig().set("arenas." + plugin.getPlayerArena(player) + ".boxSpawns." + i, null);
                    for (int j = i + 1; j <= plugin.getDataConfig().getInt("arenas." + plugin.getPlayerArena(player) + ".boxSpawnCount"); j++) {
                        dummyLocation = plugin.getDataConfig().getLocation("arenas." + plugin.getPlayerArena(player) + ".boxSpawns." + j);
                        plugin.getDataConfig().set("arenas." + plugin.getPlayerArena(player) + ".boxSpawns." + j, null);
                        plugin.getDataConfig().set("arenas." + plugin.getPlayerArena(player) + ".boxSpawns." + (j-1), dummyLocation);
                    }
                }
            } else if (e.getClickedBlock().getType().equals(Material.CHEST)) {
                for (int i = 0; i < plugin.getDataConfig().getInt("arenas." + plugin.getPlayerArena(player) + ".islandChestCount"); i++) {
                    if (e.getClickedBlock().getLocation().equals(plugin.getDataConfig().getLocation("arenas." + plugin.getPlayerArena(player) + ".islandChests." + i))) {
                        e.getClickedBlock().setType(Material.AIR);
                        plugin.getDataConfig().set("arenas." + plugin.getPlayerArena(player) + ".islandChestCount", plugin.getDataConfig().getInt("arenas." + plugin.getPlayerArena(player) + ".islandChestCount") - 1);
                        plugin.getDataConfig().set("arenas." + plugin.getPlayerArena(player) + ".islandChests." + i, null);
                        for (int j = i + 1; j <= plugin.getDataConfig().getInt("arenas." + plugin.getPlayerArena(player) + ".islandChestCount"); j++) {
                            dummyLocation = plugin.getDataConfig().getLocation("arenas." + plugin.getPlayerArena(player) + ".islandChests." + j);
                            plugin.getDataConfig().set("arenas." + plugin.getPlayerArena(player) + ".islandChests." + j, null);
                            plugin.getDataConfig().set("arenas." + plugin.getPlayerArena(player) + ".islandChests." + (j-1), dummyLocation);
                        }
                    }
                }
            } else if (e.getClickedBlock().getType().equals(Material.CHEST)) {
                for (int i = 0; i < plugin.getDataConfig().getInt("arenas." + plugin.getPlayerArena(player) + ".middleChestCount"); i++) {
                    if (e.getClickedBlock().getLocation().equals(plugin.getDataConfig().getLocation("arenas." + plugin.getPlayerArena(player) + ".middleChests." + i))) {
                        e.getClickedBlock().setType(Material.AIR);
                        plugin.getDataConfig().set("arenas." + plugin.getPlayerArena(player) + ".middleChestCount", plugin.getDataConfig().getInt("arenas." + plugin.getPlayerArena(player) + ".middleChestCount") - 1);
                        plugin.getDataConfig().set("arenas." + plugin.getPlayerArena(player) + ".middleChests." + i, null);
                        for (int j = i + 1; j <= plugin.getDataConfig().getInt("arenas." + plugin.getPlayerArena(player) + ".middleChestCount"); j++) {
                            dummyLocation = plugin.getDataConfig().getLocation("arenas." + plugin.getPlayerArena(player) + ".middleChests." + j);
                            plugin.getDataConfig().set("arenas." + plugin.getPlayerArena(player) + ".middleChests." + j, null);
                            plugin.getDataConfig().set("arenas." + plugin.getPlayerArena(player) + ".middleChests." + (j-1), dummyLocation);
                        }
                    }
                }
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
