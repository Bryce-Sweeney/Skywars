package me.CodeConduit.Skywars.commands;

import me.CodeConduit.Skywars.Main;
import me.CodeConduit.Skywars.Utils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;

public class SkywarsCreate implements CommandExecutor {
    //Variables
    private Main plugin;

    //Constructor
    public SkywarsCreate(Main plugin) {
        this.plugin = plugin;

        plugin.getCommand("sw").setExecutor(this);
    }

    @Override
    @SuppressWarnings("all")
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        Player player = (Player) sender;

        //Abort Button ItemStack Generation
        ItemStack abort = new ItemStack(Material.GRAY_DYE);
        ItemMeta abortMeta = abort.getItemMeta();
        abortMeta.setDisplayName(Utils.chat("&5&lAbort Creation"));
        abortMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        abortMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        abort.setItemMeta(abortMeta);

        //Checks if args is long enough
        if (player.hasPermission("sw.use")) {
            if (args.length != 2) {
                return false;
            }
            //Checks if player entered create
            if (args[0].equals("create")) {
                //Sets them in gui mode
                plugin.getDataConfig().set("players." + player.getUniqueId() + ".inCreationGui", "yes");
                plugin.getDataConfig().set("arenas." + args[1] + ".author", player.getName());
                plugin.getDataConfig().set("players." + player.getUniqueId() + ".workingOn", args[1]);
                //Clears their inventory and adds menu's
                player.getInventory().clear();
                player.getInventory().setItem(8, abort);
                //Saves
                try {
                    plugin.getDataConfig().save(plugin.getDataFile());
                } catch (IOException error) {
                    error.printStackTrace();
                }
                return true;
            } else if (args[0].equals("delete")) {
                //Deletes yml section for an arena


                return true;
            } else {
                return false;
            }
        } else {
            player.sendMessage(Utils.chat("&cYou do not have permission to use this command!"));
            return true;
        }
    }
}
