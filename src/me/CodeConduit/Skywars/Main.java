package me.CodeConduit.Skywars;

import me.CodeConduit.Skywars.commands.SkywarsCreate;
import me.CodeConduit.Skywars.listeners.cancelling.BreakCancel;
import me.CodeConduit.Skywars.listeners.cancelling.ClickCancel;
import me.CodeConduit.Skywars.listeners.PlayerInteractListener;
import me.CodeConduit.Skywars.listeners.cancelling.HitCancel;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
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

        boxSpawnSelectMeta.setDisplayName(Utils.chat("&6&lSpawn Locations"));
        boxSpawnSelectMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        boxSpawnSelectMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        boxSpawnSelect.setItemMeta(boxSpawnSelectMeta);
        boxSpawnSelect.setAmount(8);

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
}
