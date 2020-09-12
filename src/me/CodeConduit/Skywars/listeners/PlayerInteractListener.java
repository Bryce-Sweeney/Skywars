package me.CodeConduit.Skywars.listeners;

import jdk.nashorn.internal.ir.Block;
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
    @SuppressWarnings("unused")
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Location targetLoc = player.getTargetBlock((Set<Material>) null, 100).getLocation();

        //Abort Block
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (player.getInventory().getItemInHand().equals(Main.abort)) {
                //Take out of gui mode and delete arena file
                plugin.getDataConfig().set("players." + player.getUniqueId() + ".inGuiMode", false);
                plugin.getDataConfig().set("arenas." + plugin.getDataConfig().get("players." + player.getUniqueId() + ".currentArena"), null);
                player.getInventory().clear();
                //Notify player
                player.sendMessage(Utils.chat("&6Skywars arena creation aborted."));
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            }
        }

        //Location Targeting
        if (player.getInventory().getItemInHand().equals(Main.spawnSelect)) {
            if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                //Save values to yml
                plugin.getDataConfig().set("arenas." + plugin.getDataConfig().get("players." + player.getUniqueId() + ".currentArena") + ".spawnLoc2.X", targetLoc.getBlockX());
                plugin.getDataConfig().set("arenas." + plugin.getDataConfig().get("players." + player.getUniqueId() + ".currentArena") + ".spawnLoc2.Y", targetLoc.getBlockY());
                plugin.getDataConfig().set("arenas." + plugin.getDataConfig().get("players." + player.getUniqueId() + ".currentArena") + ".spawnLoc2.Z", targetLoc.getBlockZ());
                //Notify
                player.sendMessage(Utils.chat("&6Second location set to:&e " + targetLoc.getBlockX() + ", " + targetLoc.getBlockY() + ", " + targetLoc.getBlockZ()));
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            } else if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                //Save values to yml
                plugin.getDataConfig().set("arenas." + plugin.getDataConfig().get("players." + player.getUniqueId() + ".currentArena") + ".spawnLoc1.X", targetLoc.getBlockX());
                plugin.getDataConfig().set("arenas." + plugin.getDataConfig().get("players." + player.getUniqueId() + ".currentArena") + ".spawnLoc1.Y", targetLoc.getBlockY());
                plugin.getDataConfig().set("arenas." + plugin.getDataConfig().get("players." + player.getUniqueId() + ".currentArena") + ".spawnLoc1.Z", targetLoc.getBlockZ());
                //Notify
                player.sendMessage(Utils.chat("&6First location set to:&e " + targetLoc.getBlockX() + ", " + targetLoc.getBlockY() + ", " + targetLoc.getBlockZ()));
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            }
        }

        //Breaking Box Spawns
        if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            if (e.getClickedBlock().getType().equals(Material.DEAD_FIRE_CORAL_BLOCK) && player.getInventory().contains(Main.boxSpawnSelect)) {
                e.getClickedBlock().setType(Material.AIR);
                player.getInventory().getItemInHand().setAmount(player.getInventory().getItemInHand().getAmount() + 1);
                plugin.getDataConfig().set("arenas." + plugin.getDataConfig().get("players." + player.getUniqueId() + "currentArena") + ".nextBoxID",
                        (int) (plugin.getDataConfig().get("arenas." + plugin.getDataConfig().get("players." + player.getUniqueId() + "currentArena") + ".nextBoxID")) - 1);
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
