package com.github.MandelV.AdminShop.config;

import com.github.MandelV.AdminShop.AdminShop;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;


public class ConfigLoader {

    private FileConfiguration fc;
    private FileConfiguration fileConfiguration;
    private File f;
    private AdminShop grade;
    private String filename;

    public ConfigLoader(AdminShop grade, String filename){
        this.grade = grade;
        this.filename = filename;
        this.f = new File(this.grade.getDataFolder(), this.filename + ".yml");
    }

    public void reloadCustomConfig() {
        if(!f.exists()){
            this.grade.saveResource(this.filename + ".yml", false);
        }
        this.fc = YamlConfiguration.loadConfiguration(f);
    }

    public FileConfiguration getCustomConfig() {
        if (fc == null) {
            this.reloadCustomConfig();
        }
        return this.fc;
    }

    public File getF(){
        return f;
    }


}
