package com.github.MandelV.AdminShop.Commands;

import Dao.Request;
import com.github.MandelV.AdminShop.AdminShop;
import com.github.MandelV.AdminShop.tools.ChatFormatting;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class ModoCmds extends Commands {

    private boolean return_cmd = false;
    public ModoCmds(AdminShop grade){
        super(grade);
    }



    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if(commandSender.hasPermission("grade.moderateur")){

            if(args.length == 0)
               return this.command_help(commandSender);


            if(args[0].equalsIgnoreCase("add")){

                this.return_cmd = this.command_add(commandSender, args);

            }else if(args[0].equalsIgnoreCase("remove")){

                this.return_cmd = this.command_remove(commandSender, args);

            }else if(args[0].equalsIgnoreCase("info")){

                if(args.length > 1)
                    this.return_cmd = this.command_info(commandSender, args);

            }else{
                command_help(commandSender);
            }
        }else {
            commandSender.sendMessage(this.prefix + ChatFormatting.formatText(grade.getMessage().getCustomConfig().getString("permission_deny")));
        }



        if(!this.return_cmd){
            this.command_help(commandSender);
        }
        return true;
    }


    @Override
    protected boolean command_help(CommandSender sender) {
        if(!(sender instanceof Player)){
            grade.getServer().getConsoleSender().sendMessage(this.prefix + ChatFormatting.formatText(grade.getMessage().getCustomConfig().getString("error_not_console_cmd")));
            return true;
        }

        String listhelp =  grade.getMessage().getCustomConfig().getStringList("help_modo").get(0);
        sender.sendMessage(this.prefix + ChatFormatting.formatText(listhelp));
        return true;
    }

    private boolean command_add(CommandSender sender, String[] args){
        if(!(sender instanceof Player)){
            grade.getServer().getConsoleSender().sendMessage(this.prefix + ChatFormatting.formatText(grade.getMessage().getCustomConfig().getString("error_not_console_cmd")));
            return true;
        }
        if(args.length < 3){

            return false;
        }
        //grademodo add Pseudo GRADE

        String pseudo = args[1];
        String Grade_cmd = args[2];


        String cmdResetGrade = grade.getConf().getCustomConfig().getString("command_reset_grade");
        String gradeValide = "null";
        String gradecmds = "null";
        String gradename = "null";
        double gradeprice = 0.0;
        int gradeID = -1;
        ConfigurationSection ymlgrade = grade.getConf().getCustomConfig().getConfigurationSection("AdminShop");

        for(String element : ymlgrade.getKeys(false)) {

            if(Grade_cmd.equalsIgnoreCase(element)){
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

        //if player has already sub staff_player_has_sub
        if(Request.hasSubscribe(pseudo)){

            sender.sendMessage(this.prefix + ChatFormatting.formatText(grade.getMessage().getCustomConfig().getString("staff_player_has_sub")));
            return true;
        }

        //Add database entry grade_add_by_staff
        if(!Request.addGrade(pseudo, gradeID)){
            sender.sendMessage(this.prefix + ChatFormatting.formatText(grade.getMessage().getCustomConfig().getString("staff_player_has_no_account")));
            return true;

        }

        //Execute command

        cmdResetGrade = cmdResetGrade.replace("{PSEUDO}", pseudo);
        gradecmds = gradecmds.replace("{PSEUDO}", pseudo);
        grade.getServer().dispatchCommand(grade.getServer().getConsoleSender(), cmdResetGrade);
        grade.getServer().dispatchCommand(grade.getServer().getConsoleSender(), gradecmds);


        //SuccessFull message

        if(grade.getServer().getOfflinePlayer(pseudo).isOnline()){
            String send_message_player = grade.getMessage().getCustomConfig().getString("grade_claim");

            send_message_player = send_message_player.replace("{GRADE}", gradename);
            grade.getServer().getPlayer(pseudo).sendMessage(this.prefix + ChatFormatting.formatText(send_message_player));

        }
        String send_message_staff = grade.getMessage().getCustomConfig().getString("grade_add_by_staff");
        send_message_staff = send_message_staff.replace("{GRADE}", gradename);
        send_message_staff = send_message_staff.replace("{PSEUDO}", pseudo);
        sender.sendMessage(this.prefix + ChatFormatting.formatText(send_message_staff));


        //SuccessFull message


        return true;
    }

    private boolean command_remove(CommandSender sender, String[] args){
        if(!(sender instanceof Player)){
            grade.getServer().getConsoleSender().sendMessage(this.prefix + ChatFormatting.formatText(grade.getMessage().getCustomConfig().getString("error_not_console_cmd")));
            return true;
        }
        if(args.length < 2){
            return false;
        }
        String cmdResetGrade = grade.getConf().getCustomConfig().getString("command_reset_grade");
        String pseudo = args[1];

        //If player has subcribe
        if(!Request.hasSubscribe(pseudo)){
            sender.sendMessage(this.prefix + ChatFormatting.formatText(grade.getMessage().getCustomConfig().getString("staff_player_has_no_sub")));
            return true;
        }

        Request.remove(pseudo);

        //Execute command
         cmdResetGrade = cmdResetGrade.replace("{PSEUDO}", pseudo);
        grade.getServer().dispatchCommand(grade.getServer().getConsoleSender(), cmdResetGrade);


        //Notif
        String notif_staff = grade.getMessage().getCustomConfig().getString("grade_remove_staff");
        notif_staff = notif_staff.replace("{PSEUDO}", pseudo);

        if(grade.getServer().getOfflinePlayer(pseudo).isOnline()){
            String notif_player = grade.getMessage().getCustomConfig().getString("grade_remove_by_staff");
            grade.getServer().getPlayer(pseudo).sendMessage(this.prefix + ChatFormatting.formatText(notif_player));
        }
        sender.sendMessage(this.prefix + ChatFormatting.formatText(notif_staff));
        return true;
    }

    private boolean command_info(CommandSender sender, String[] args){

        String pseudo = args[1];

        if(!(sender instanceof Player)){
            grade.getServer().getConsoleSender().sendMessage(this.prefix + ChatFormatting.formatText(grade.getMessage().getCustomConfig().getString("error_not_console_cmd")));
            return true;
        }
        if(args.length < 2){
            return false;
        }

        //If player has subcribe
        if(!Request.hasSubscribe(pseudo)){
            sender.sendMessage(this.prefix + ChatFormatting.formatText(grade.getMessage().getCustomConfig().getString("staff_player_has_no_sub")));
            return true;
        }

        List<String> infoGrade = grade.getMessage().getCustomConfig().getStringList("gradeInfo");

        Map<String, String> gradePlayer = Request.getGrade(pseudo);

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
}
