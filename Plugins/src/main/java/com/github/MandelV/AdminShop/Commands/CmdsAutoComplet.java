package com.github.MandelV.AdminShop.Commands;

import com.github.MandelV.AdminShop.AdminShop;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import java.util.ArrayList;
import java.util.List;

public class CmdsAutoComplet implements TabCompleter {

    private AdminShop grade;
    private List<String> arg1;


    public CmdsAutoComplet(AdminShop adminShop){
        this.grade = adminShop;
        this.arg1 = new ArrayList<>();

        this.arg1.add("reload");
        this.arg1.add("categorie");
        this.arg1.add("item");
        this.arg1.add("editmode");
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {

        List<String> completion = new ArrayList<>();

        if(args.length < 2){
        }
        return completion;
    }
}
