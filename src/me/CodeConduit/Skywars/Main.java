package me.CodeConduit.Skywars;

import me.CodeConduit.Skywars.commands.SkywarsCreate;
import me.CodeConduit.Skywars.listeners.PlayerInteractListener;
import me.CodeConduit.Skywars.listeners.cancelling.BreakCancel;
import me.CodeConduit.Skywars.listeners.cancelling.ClickCancel;
import me.CodeConduit.Skywars.listeners.cancelling.HitCancel;
import me.CodeConduit.Skywars.listeners.cancelling.PlaceCancel;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Main extends JavaPlugin {
    //Variables
    private final File dataFile = new File(getDataFolder(), "data.yml");
    private final FileConfiguration dataConfig = YamlConfiguration.loadConfiguration(dataFile);

    //Create itemstacks for gui mode
    public static ItemStack abort = new ItemStack(Material.GRAY_DYE);
    public static ItemMeta abortMeta = abort.getItemMeta();

    public static ItemStack spawnSelect = new ItemStack(Material.BLAZE_ROD);
    public static ItemMeta spawnSelectMeta = spawnSelect.getItemMeta();

    public static ItemStack boxSpawnSelect = new ItemStack(Material.DEAD_FIRE_CORAL_BLOCK);
    public static ItemMeta boxSpawnSelectMeta = boxSpawnSelect.getItemMeta();

    public static ItemStack islandChestSelect = new ItemStack(Material.CHEST);
    public static ItemMeta islandChestSelectMeta = islandChestSelect.getItemMeta();

    public static ItemStack middleChestSelect = new ItemStack(Material.TRAPPED_CHEST);
    public static ItemMeta middleChestSelectMeta = middleChestSelect.getItemMeta();

    public static ItemStack soloMode = new ItemStack(Material.LIME_DYE);
    public static ItemMeta soloModeMeta = soloMode.getItemMeta();

    public static ItemStack duoMode = new ItemStack(Material.PINK_DYE);
    public static ItemMeta duoModeMeta = soloMode.getItemMeta();

    public static ItemStack confirm = new ItemStack(Material.LIME_DYE);
    public static ItemMeta confirmMeta = soloMode.getItemMeta();

    //Enables when the plugin is enabled
    public void onEnable() {
        //Perform actions on itemMeta
        abortMeta.setDisplayName(Utils.chat("&6&lAbort Creation"));
        abortMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        abortMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        abort.setItemMeta(abortMeta);

        spawnSelectMeta.setDisplayName(Utils.chat("&6&lSelect Lobby Spawn"));
        spawnSelectMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        spawnSelectMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        spawnSelect.setItemMeta(spawnSelectMeta);

        boxSpawnSelectMeta.setDisplayName(Utils.chat("&c&lSpawn Locations"));
        boxSpawnSelectMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        boxSpawnSelectMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        boxSpawnSelect.setItemMeta(boxSpawnSelectMeta);

        islandChestSelectMeta.setDisplayName(Utils.chat("&c&lIsland Chest"));
        islandChestSelectMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        islandChestSelectMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        islandChestSelect.setItemMeta(islandChestSelectMeta);

        middleChestSelectMeta.setDisplayName(Utils.chat("&c&lMid Chest"));
        middleChestSelectMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        middleChestSelectMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        middleChestSelect.setItemMeta(middleChestSelectMeta);

        soloModeMeta.setDisplayName(Utils.chat("&6&lSolo Mode"));
        soloModeMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        soloModeMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        soloMode.setItemMeta(soloModeMeta);

        duoModeMeta.setDisplayName(Utils.chat("&6&lDuo Mode"));
        duoModeMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        duoModeMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        duoMode.setItemMeta(duoModeMeta);

        confirmMeta.setDisplayName(Utils.chat("&6&lConfirm"));
        confirmMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        confirmMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        confirm.setItemMeta(confirmMeta);

        //Enable plugins
        if (!dataFile.exists()) {
            saveResource("data.yml", false);
        }
        enableListeners(true);
        enableCommands(true);
    }

    //Enables when the plugin is disabled
    public void onDisable() {
        try {
            getDataConfig().save(getDataFile());
        } catch (IOException error) {
            error.printStackTrace();
        }
    }

    //Method for enabling listeners
    public void enableListeners(boolean enable) {
        if (enable) {
            //Enable classes here
            new ClickCancel(this);
            new BreakCancel(this);
            new HitCancel(this);
            new PlaceCancel(this);
            new PlayerInteractListener(this);
        }
    }
    //Method for enabling commands
    public void enableCommands(boolean enable) {
        if (enable) {
            //Enable classes here
            new SkywarsCreate(this);
        }
    }

    //Getters for data
    public FileConfiguration getDataConfig() {
        return dataConfig;
    }
    public File getDataFile() {
        return dataFile;
    }
    public String getPlayerArena(Player player) {
        return String.valueOf(getDataConfig().get("players." + player.getUniqueId() + ".currentArena"));
    }
}
