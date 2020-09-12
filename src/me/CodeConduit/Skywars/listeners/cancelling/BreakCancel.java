package me.CodeConduit.Skywars.listeners.cancelling;

import me.CodeConduit.Skywars.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Objects;

public class BreakCancel implements Listener {
    //Variables
    private final Main plugin;

    //Constructor
    public BreakCancel(Main plugin) {
        this.plugin = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    //Event Handler
    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (Objects.equals(plugin.getDataConfig().get("players." + player.getUniqueId() + ".inGuiMode"), true)) {
            e.setCancelled(true);
        }
    }
}
