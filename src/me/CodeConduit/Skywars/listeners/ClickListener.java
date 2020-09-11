package me.CodeConduit.Skywars.listeners;

import me.CodeConduit.Skywars.Main;
import me.CodeConduit.Skywars.Utils;
import me.CodeConduit.Skywars.commands.SkywarsCreate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;

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

        //Abort Button ItemStack Generation
        ItemStack abort = new ItemStack(Material.GRAY_DYE);
        ItemMeta abortMeta = abort.getItemMeta();
        abortMeta.setDisplayName(Utils.chat("&5&lAbort Creation"));
        abortMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        abortMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        abort.setItemMeta(abortMeta);

        if (plugin.getPlayerInGui(player)) {
            if (e.isRightClick()) {
                if (player.getItemOnCursor().equals(abort)) {
                    player.getInventory().clear();
                    for (int i = 0; i < 36; i++) {
                        player.getInventory().setItem(i, (ItemStack) plugin.getDataConfig().get("players." + player.getUniqueId() + ".savedInv." + i));
                    }

                    plugin.setPlayerInGui(player, false);
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                    player.sendMessage(Utils.chat("&b&lCancelled creation!"));
                }
                try {
                    plugin.getDataConfig().save(plugin.getDataFile());
                } catch (IOException error) {
                    error.printStackTrace();
                }
            }
            e.setCancelled(true);
        }
    }
}
