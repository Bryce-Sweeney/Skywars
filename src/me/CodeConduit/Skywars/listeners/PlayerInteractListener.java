package me.CodeConduit.Skywars.listeners;

import me.CodeConduit.Skywars.Main;
import me.CodeConduit.Skywars.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;

public class PlayerInteractListener implements Listener {
    //Variables
    private Main plugin;
    //Constructor
    public PlayerInteractListener(Main plugin) {
        this.plugin = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    //Event handler
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        
        //Abort Button ItemStack Copypasta
        ItemStack abort = new ItemStack(Material.GRAY_DYE);
        ItemMeta abortMeta = abort.getItemMeta();
        abortMeta.setDisplayName(Utils.chat("&5&lAbort Creation"));
        abortMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        abortMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        abort.setItemMeta(abortMeta);


        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (player.getInventory().getItemInHand().equals(abort)) {
                //Turn off gui mode
                plugin.getDataConfig().set("players." + player.getUniqueId() + ".inCreationGui", "no");
                //Restore inventory
                player.getInventory().clear();
                for (int i = 0; i < 36; i++) {
                    player.getInventory().setItem(i, (ItemStack) plugin.getDataConfig().get("players." + player.getUniqueId() + ".savedInv." + i));
                }
                //Delete yaml
                plugin.getDataConfig().set("arenas." + plugin.getDataConfig().get("players." + player.getUniqueId() + ".workingOn"), null);

                //Notify player
                player.sendMessage(Utils.chat("&6&lSkywars game creation cancelled."));
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);

                //Save

            }
        }
    }
}
