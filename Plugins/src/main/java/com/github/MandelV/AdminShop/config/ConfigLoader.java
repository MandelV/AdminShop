package com.github.MandelV.AdminShop.config;

import com.github.MandelV.AdminShop.AdminShop;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;


public class ConfigLoader {

    private FileConfiguration fc;
    //private FileConfiguration fileConfiguration;
    private File f;
    private AdminShop adminShop;
    private String filename;

    ConfigLoader(AdminShop adminShop, String filename){
        this.adminShop = adminShop;
        this.filename = filename;
        this.f = new File(this.adminShop.getDataFolder(), this.filename + ".yml");
    }

    public void reloadCustomConfig() {
        if(!f.exists()){
            this.adminShop.saveResource(this.filename + ".yml", false);
        }
        this.fc = YamlConfiguration.loadConfiguration(f);
    }

    public FileConfiguration getCustomConfig() {
        if (fc == null) {
            this.reloadCustomConfig();
        }
        return this.fc;
    }

   /* public File getF(){
        return f;
    }*/


}
