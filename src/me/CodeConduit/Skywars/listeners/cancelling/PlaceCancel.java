package me.CodeConduit.Skywars.listeners.cancelling;

import me.CodeConduit.Skywars.Main;
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
            } else if (e.getBlockPlaced().getType().equals(Material.CHEST)) {

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
