package com.github.MandelV.AdminShop.Commands;

import com.github.MandelV.AdminShop.AdminShop;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class CmdsAutoComplet implements TabCompleter {

    private AdminShop grade;

    public CmdsAutoComplet(AdminShop grade){
        this.grade = grade;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {

        List<String> completion = new ArrayList<>();


        switch (command.getName()){
            case "grade":
                completion.clear();
                completion.add("buy");
                completion.add("info");
                completion.add("remove");
                completion.add("about");


                if(args[0].equalsIgnoreCase("buy")){

                    completion.clear();
                    ConfigurationSection ymlgrade = grade.getConf().getCustomConfig().getConfigurationSection("AdminShop");

                    for(String element : ymlgrade.getKeys(false)){
                        completion.add(element);
                        commandSender.sendMessage(element);
                    }
                }

                break;
            case "grademodo":
                break;
            case "gradeadmin":
                break;
        }
        return completion;
    }
}
