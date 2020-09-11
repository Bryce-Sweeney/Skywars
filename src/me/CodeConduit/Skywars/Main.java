package me.CodeConduit.Skywars;

import me.CodeConduit.Skywars.commands.SkywarsCreate;
import me.CodeConduit.Skywars.listeners.ClickListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Main extends JavaPlugin {
    //Variables
    private File dataFile = new File(getDataFolder(), "playerdata.yml");
    private FileConfiguration dataConfig = YamlConfiguration.loadConfiguration(dataFile);

    //Enables when the plugin is enabled
    public void onEnable() {
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
            new ClickListener(this);
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

    //Ease of use getters for data
    public boolean getPlayerInGui(Player player) {
        return (boolean) getDataConfig().get("players." + player.getUniqueId() + ".inCreationGui");
    }
    public void setPlayerInGui(Player player, boolean bool) {
        getDataConfig().set("players." + player.getUniqueId() + ".inCreationGui", bool);
    }
}
