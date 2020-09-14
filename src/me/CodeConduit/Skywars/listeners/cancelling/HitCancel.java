package me.CodeConduit.Skywars.listeners.cancelling;

import me.CodeConduit.Skywars.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class HitCancel implements Listener {
    //Variables
    private final Main plugin;
    //Constructor
    public HitCancel(Main plugin) {
        this.plugin = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    //Event handler
    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        Player attacker = (Player) e.getDamager();
        Player victim = (Player) e.getEntity();
        if (plugin.getDataConfig().getBoolean("players." + victim.getUniqueId() + ".inGuiMode") || plugin.getDataConfig().getBoolean("players." + attacker.getUniqueId() + ".inGuiMode"))
            e.setCancelled(true);
    }
}
