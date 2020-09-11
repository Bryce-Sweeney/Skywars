package me.CodeConduit.Skywars.listeners;

import me.CodeConduit.Skywars.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;

public class SavePlayer implements Listener {
    //Variables
    private Main plugin;

    //Constructor
    public SavePlayer(Main plugin) {
        this.plugin = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    //Event Handler
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = (Player) e.getPlayer();
        if (plugin.getDataConfig().get("players." + player.getUniqueId() + ".inCreationGui") == null) {
            plugin.getDataConfig().set("players." + player.getUniqueId() + ".inCreationGui", false);
        }
        try {
            plugin.getDataConfig().save(plugin.getDataFile());
        } catch (IOException error) {
            error.printStackTrace();
        }
    }
}
