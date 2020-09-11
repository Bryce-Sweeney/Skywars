package me.CodeConduit.Skywars.listeners;

import me.CodeConduit.Skywars.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class SavePlayer implements Listener {
    //Variables
    private Main plugin;
    
    //Constructor
    public SavePlayer(Main plugin) {
        this.plugin = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
}
