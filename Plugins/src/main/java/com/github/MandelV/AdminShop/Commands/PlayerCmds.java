package com.github.MandelV.AdminShop.Commands;

import Dao.Request;
import com.github.MandelV.AdminShop.AdminShop;
import com.github.MandelV.AdminShop.tools.ChatFormatting;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerCmds extends Commands {

    private boolean return_cmd = false;
    public PlayerCmds(AdminShop grade){
        super(grade);
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if(commandSender.hasPermission("grade.player")){

            if(args.length == 0)
                return this.command_help(commandSender);

            if(args[0].equalsIgnoreCase("buy")){

                this.return_cmd = this.command_buy(commandSender, args);

            }else if(args[0].equalsIgnoreCase("info")){

                this.return_cmd = this.command_info(commandSender);
            }else if (args[0].equalsIgnoreCase("remove")){

                this.return_cmd = this.command_remove(commandSender);

            }else if (args[0].equalsIgnoreCase("about")) {

                this.return_cmd = this.command_about(commandSender);

            } else{
                this.return_cmd = this.command_help(commandSender);
            }
        }else {
            commandSender.sendMessage( this.prefix + ChatFormatting.formatText(grade.getMessage().getCustomConfig().getString("permission_deny")));
        }

        if(!this.return_cmd){
            this.command_help(commandSender);
        }
        return true;
    }


    @Override
    protected boolean command_help(CommandSender sender){

        sender.sendMessage(ChatFormatting.formatText("&e========== &4&lGRADE &e=========="));
        sender.sendMessage("");

        ConfigurationSection ymlgrade = grade.getConf().getCustomConfig().getConfigurationSection("AdminShop");
        String monnaie = grade.getConf().getCustomConfig().getString("Economy.nameMonnaie");

        for(String element : ymlgrade.getKeys(false)){
            String gradename = ymlgrade.getString(element + ".name");
            double gradeprice = ymlgrade.getDouble(element + ".price");

            sender.sendMessage(ChatFormatting.formatText(gradename + "&f : ") + ChatFormatting.formatText("&e" + String.valueOf(gradeprice)) + monnaie);
        }

        sender.sendMessage("");
        grade.getMessage().getCustomConfig().getStringList("help").forEach((v) -> sender.sendMessage(ChatFormatting.formatText(v)));
        sender.sendMessage("");

        sender.sendMessage(ChatFormatting.formatText("&e==========================="));
        return true;
    }

    private boolean command_buy(CommandSender sender, String[] args){
        if(!(sender instanceof Player)){
            grade.getServer().getConsoleSender().sendMessage(this.prefix + ChatFormatting.formatText(grade.getMessage().getCustomConfig().getString("error_not_console_cmd")));
            return true;
        }
        if(args.length < 2){
            this.command_help(sender);
            return false;
        }

        String cmdResetGrade = grade.getConf().getCustomConfig().getString("command_reset_grade");
        String gradeValide = "null";
        String gradecmds = "null";
        String gradename = "null";
        double gradeprice = 0.0;
        int gradeID = -1;
        ConfigurationSection ymlgrade = grade.getConf().getCustomConfig().getConfigurationSection("AdminShop");

        for(String element : ymlgrade.getKeys(false)) {
            if(args[1].equalsIgnoreCase(element)){
                gradeValide = element;
                gradeprice = ymlgrade.getDouble(element + ".price");
                gradeID = ymlgrade.getInt(element + ".bddID");
                gradecmds = ymlgrade.getString(element + ".cmds");
                gradename = ymlgrade.getString(element + ".name");
            }
        }
        //if grade is valide
        if(gradeValide.equalsIgnoreCase("null")){
            sender.sendMessage(this.prefix + ChatFormatting.formatText(grade.getMessage().getCustomConfig().getString("grade_does_not_exist")));
            return false;
        }

        //if player has required money
        if(!AdminShop.econ.has(grade.getServer().getPlayer(sender.getName()), gradeprice)){
            sender.sendMessage(this.prefix + ChatFormatting.formatText(grade.getMessage().getCustomConfig().getString("player_no_required_money")));
            return true;
        }

        //if player has already sub
        if(Request.hasSubscribe(sender.getName())){
            sender.sendMessage(this.prefix + ChatFormatting.formatText(grade.getMessage().getCustomConfig().getString("player_has_sub")));
            return true;
        }

        //Add database entry
        if(!Request.addGrade(sender.getName(), gradeID)){
            sender.sendMessage(this.prefix + ChatFormatting.formatText(grade.getMessage().getCustomConfig().getString("no_website_account")));
            return true;

        }

        //WithDraw
        AdminShop.econ.withdrawPlayer(grade.getServer().getPlayer(sender.getName()), gradeprice);

        //Execute command
        cmdResetGrade = cmdResetGrade.replace("{PSEUDO}", sender.getName());
        gradecmds = gradecmds.replace("{PSEUDO}", sender.getName());
        grade.getServer().dispatchCommand(grade.getServer().getConsoleSender(), cmdResetGrade);
        grade.getServer().dispatchCommand(grade.getServer().getConsoleSender(), gradecmds);


        //SuccessFull message
        String message_claim = grade.getMessage().getCustomConfig().getString("grade_claim");
        message_claim = message_claim.replace("{GRADE}", gradename);
        sender.sendMessage(this.prefix + ChatFormatting.formatText(message_claim));

        return true;
    }

    private boolean command_remove(CommandSender sender){

        String cmdResetGrade = grade.getConf().getCustomConfig().getString("command_reset_grade");
        if(!(sender instanceof Player)){
            grade.getServer().getConsoleSender().sendMessage(this.prefix + ChatFormatting.formatText(grade.getMessage().getCustomConfig().getString("error_not_console_cmd")));
            return true;
        }
        //If player has subcribe
        if(!Request.hasSubscribe(sender.getName())){
            sender.sendMessage(this.prefix + ChatFormatting.formatText(grade.getMessage().getCustomConfig().getString("player_has_no_sub")));
            return true;
        }

        Request.remove(sender.getName());
        //Execute command
        cmdResetGrade = cmdResetGrade.replace("{PSEUDO}", sender.getName());
        grade.getServer().dispatchCommand(grade.getServer().getConsoleSender(), cmdResetGrade);
        //grade_remove
        sender.sendMessage(this.prefix + ChatFormatting.formatText(grade.getMessage().getCustomConfig().getString("grade_remove")));
        return true;
    }

    private boolean command_info(CommandSender sender){

        if(!(sender instanceof Player)){
            grade.getServer().getConsoleSender().sendMessage(this.prefix + ChatFormatting.formatText(grade.getMessage().getCustomConfig().getString("error_not_console_cmd")));
            return true;
        }

        //If player has subcribe
        if(!Request.hasSubscribe(sender.getName())){
            sender.sendMessage(this.prefix + ChatFormatting.formatText(grade.getMessage().getCustomConfig().getString("player_has_no_sub")));
            return true;
        }

        List<String> infoGrade = grade.getMessage().getCustomConfig().getStringList("gradeInfo");

        Map<String, String> gradePlayer = Request.getGrade(sender.getName());

        //Display information
        for(String info : infoGrade){

            info = info.replace("{GRADE}", gradePlayer.get("nameGrade"));
            info = info.replace("{DATE_DEBUT}", gradePlayer.get("dateStart"));
            info = info.replace("{DATE_FIN}", gradePlayer.get("dateEnd"));
            info = info.replace("{JOURS}", gradePlayer.get("DayRemaining"));

            sender.sendMessage(ChatFormatting.formatText(info));

        }
        return true;
    }

    private boolean command_about(CommandSender sender){

        sender.sendMessage(this.prefix + ChatFormatting.formatText("&4Grade &6by Akitoshi ") + ChatFormatting.formatText("&aTallys.fr"));
        return true;
    }

}
