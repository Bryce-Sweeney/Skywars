package me.CodeConduit.Skywars.listeners;

import me.CodeConduit.Skywars.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Objects;

public class ClickListener implements Listener {
    //Variables
    private Main plugin;

    //Constructor
    public ClickListener(Main plugin) {
        this.plugin = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    //Event Handler
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        //Assigning humanEntity to player
        Player player = (Player) e.getWhoClicked();
        String inGui = (String) plugin.getDataConfig().get("players." + player.getUniqueId() + ".inCreationGui");

        //If player is in gui mode, then dont allow them to move the item
        if (inGui.equals("yes")) {
            e.setCancelled(true);
        }
    }
}
