package me.CodeConduit.Skywars.listeners.cancelling;

import me.CodeConduit.Skywars.Main;
import org.bukkit.Bukkit;
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
        if (Objects.equals(plugin.getDataConfig().get("players." + player.getUniqueId() + ".inGuiMode"), true)) {
            e.setCancelled(true);
            if (e.getBlockPlaced().getType().equals(Material.DEAD_FIRE_CORAL_BLOCK)) {
                e.setCancelled(false);
                player.getInventory().getItemInHand().setAmount(player.getInventory().getItemInHand().getAmount() - 1);
                plugin.getDataConfig().set(
                        "arenas."
                        + plugin.getDataConfig().get("players." + player.getUniqueId() + "currentArena")
                        + ".boxSpawns.",
                        plugin.getDataConfig().get("arenas." + plugin.getDataConfig().get("players." + player.getUniqueId() + "currentArena") + ".nextBoxID"));
                plugin.getDataConfig().set("arenas." + plugin.getDataConfig().get("players." + player.getUniqueId() + "currentArena") + ".nextBoxID",
                        (int) (plugin.getDataConfig().get("arenas." + plugin.getDataConfig().get("players." + player.getUniqueId() + "currentArena") + ".nextBoxID")) + 1);
                //Save
                try {
                    plugin.getDataConfig().save(plugin.getDataFile());
                } catch (IOException error) {
                    error.printStackTrace();
                }
            }
        }
    }
}
