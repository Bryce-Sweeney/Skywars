package me.CodeConduit.Skywars.commands;

import me.CodeConduit.Skywars.Main;
import me.CodeConduit.Skywars.Utils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Objects;

public class SkywarsCreate implements CommandExecutor {
    //Variables
    private final Main plugin;

    //Constructor
    public SkywarsCreate(Main plugin) {
        this.plugin = plugin;

        plugin.getCommand("sw").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        //Variables
        Player player = (Player) sender;
        //Do they have permission?
        if (player.hasPermission("sw.use")) {
            //Did they input the correct number of variables?
            if (args.length == 2) {
                //Did they input create, delete, or other?
                if (args[0].equals("create")) {
                    //Create an arena and set the player to gui mode
                    plugin.getDataConfig().set("players." + player.getUniqueId() + ".inGuiMode", true);
                    plugin.getDataConfig().set("players." + player.getUniqueId() + ".currentArena", args[1]);
                    plugin.getDataConfig().set("arenas." + args[1] + ".creator", String.valueOf(player.getUniqueId()));
                    plugin.getDataConfig().set("arenas." + args[1] + ".world", player.getWorld().getName());
                    plugin.getDataConfig().set("arenas." + args[1] + ".boxSpawnCount", 0);
                    plugin.getDataConfig().set("arenas." + args[1] + ".islandChestCount", 0);
                    plugin.getDataConfig().set("arenas." + args[1] + ".middleChestCount", 0);
                    plugin.getDataConfig().set("arenas." + args[1] + ".isSolo", true);
                    //Give player items
                    for (int i = 0; i < 36; i++) {
                        if (!(player.getInventory().getItem(i) == null))
                            player.getInventory().getItem(i).setAmount(0);
                    }
                    player.getInventory().setItem(8, Main.abort);
                    player.getInventory().setItem(0, Main.spawnSelect);
                    player.getInventory().setItem(1, Main.soloMode);
                    player.getInventory().setItem(3, Main.boxSpawnSelect);
                    player.getInventory().setItem(4, Main.islandChestSelect);
                    player.getInventory().setItem(5, Main.middleChestSelect);
                    player.getInventory().setItem(7, Main.confirm);
                    //Save
                    try {
                        plugin.getDataConfig().save(plugin.getDataFile());
                    } catch (IOException error) {
                        error.printStackTrace();
                    }
                    return true;
                } else if (args[0].equals("delete")) {
                    //Delete chests and spawns
                    try {
                        for (int i = 0; i < plugin.getDataConfig().getInt("arenas." + plugin.getPlayerArena(player) + ".boxSpawnCount"); i++) {
                            Objects.requireNonNull(plugin.getDataConfig().getLocation("arenas." + plugin.getPlayerArena(player) + ".boxSpawns." + i)).getBlock().setType(Material.AIR);
                        }
                        for (int i = 0; i < plugin.getDataConfig().getInt("arenas." + plugin.getPlayerArena(player) + ".islandChestCount"); i++) {
                            Objects.requireNonNull(plugin.getDataConfig().getLocation("arenas." + plugin.getPlayerArena(player) + ".islandChests." + i)).getBlock().setType(Material.AIR);
                        }
                        for (int i = 0; i < plugin.getDataConfig().getInt("arenas." + plugin.getPlayerArena(player) + ".middleChestCount"); i++) {
                            Objects.requireNonNull(plugin.getDataConfig().getLocation("arenas." + plugin.getPlayerArena(player) + ".middleChests." + i)).getBlock().setType(Material.AIR);
                        }
                        plugin.getDataConfig().set("arenas." + args[1], null);
                        //Notify player
                        player.sendMessage(Utils.chat("&6Arena successfully deleted!"));
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                        //Save
                        try {
                            plugin.getDataConfig().save(plugin.getDataFile());
                        } catch (IOException error) {
                            error.printStackTrace();
                        }
                        return true;
                    } catch (NullPointerException e) {
                        player.sendMessage(Utils.chat("&6That arena does not exist!"));
                        return true;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            player.sendMessage(Utils.chat("&cYou do not have permission to use this command!"));
            return true;
        }
    }
}
