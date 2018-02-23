package com.github.MandelV.AdminShop.Commands;


import Dao.Dao;
import Dao.Dao_Categorie;
import Dao.Dao_item;
import Dao.Request;
import com.github.MandelV.AdminShop.AdminShop;
import com.github.MandelV.AdminShop.Economy.EcoGuiFactory;
import com.github.MandelV.AdminShop.Economy.EcoItem;
import com.github.MandelV.AdminShop.Economy.ItemStatut;
import com.github.MandelV.AdminShop.GUI.Gui;
import com.github.MandelV.AdminShop.GUI.GuiInvRow;
import com.github.MandelV.AdminShop.GUI.GuiManager;
import com.github.MandelV.ChatFormatting.tools.ChatFormatting;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

/**
 * @author MandelV
 * @version 1.0
 * Represente all command of plugin AdminShop
 * @see Commands
 */
public class PlayerCmds extends Commands {

    private boolean return_cmd = false;

    public PlayerCmds(AdminShop adminShop) {
        super(adminShop);
    }

    /**
     * @param commandSender Sender command
     * @param command       command
     * @param s             .
     * @param args          Command's argument
     * @return if the command is correctly executed
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if (command.getName().equalsIgnoreCase("adminshop") && args.length == 0) {

            return this.openAdminShop(commandSender);


        } else if (args[0].equalsIgnoreCase("listcategorie")) {

            return this.listCategorie(commandSender);

        } else if (args[0].equalsIgnoreCase("categorie") && (args.length > 1)) {

            if (args[1].equalsIgnoreCase("add")) {
                return this.addCategorie(commandSender, args);
            }

        } else if (args[0].equalsIgnoreCase("item") && (args.length > 1)) {

            if (args[1].equalsIgnoreCase("add")) {
                return this.addItemIntoCategorie(commandSender, args);
            }

        } else if (args[0].equalsIgnoreCase("editmode")) {
            return this.toggleEditMode(commandSender);

        } else if (args[0].equalsIgnoreCase("reload")) {
            return this.reload(commandSender);
        }else if(args[0].equalsIgnoreCase("economy")){
            this.total(commandSender, args);
        }

        return true;
    }

    @Override
    protected boolean command_help(CommandSender sender) {
        return false;
    }

    /**
     * @param sender Command Sender
     * @return if command is correctly executed
     */
    private boolean openAdminShop(CommandSender sender) {

        if (!sender.hasPermission("adminshop.open")) {
            sender.sendMessage(ChatFormatting.formatText(adminShop.getMessage().getCustomConfig().getString("prefix")) +
                    ChatFormatting.formatText(adminShop.getMessage().getCustomConfig().getString("permission_deny")));
            return true;
        }
        adminShop.shop.open(((Player) sender));

        return true;
    }

    /**
     * @param sender Command Sender
     * @return if command is correctly executed
     */
    private boolean listCategorie(CommandSender sender) {
        if (!sender.hasPermission("adminshop.categorie.listcategorie")) {
            sender.sendMessage(ChatFormatting.formatText(adminShop.getMessage().getCustomConfig().getString("prefix")) +
                    ChatFormatting.formatText(adminShop.getMessage().getCustomConfig().getString("permission_deny")));
            return true;
        }

        sender.sendMessage(ChatFormatting.formatText("[&6AdminShop&f] &2Nombre de categorie : &6" + adminShop.categories.size()));
        adminShop.categories.forEach(cat -> sender.sendMessage(ChatFormatting.formatText("&6" + cat.getName())));
        return true;
    }

    /**
     * @param sender Command Sender
     * @param args   Command arguments
     * @return if command is correctly executed
     */
    private boolean addCategorie(CommandSender sender, String[] args) {
        if (!sender.hasPermission("adminshop.categorie.add")) {
            sender.sendMessage(ChatFormatting.formatText(adminShop.getMessage().getCustomConfig().getString("prefix"))
                    + ChatFormatting.formatText(adminShop.getMessage().getCustomConfig().getString("permission_deny")));
            return true;
        }
        if (args.length == 4) {
            ItemStack itemHolder = ((Player) sender).getInventory().getItemInMainHand();
            ((Player) sender).getOpenInventory().getBottomInventory().remove(itemHolder);

            if (itemHolder.getType() != Material.AIR) {
                String name = (!args[2].contains("&")) ? args[2] : null;
                String displayname = args[3];
                Boolean ifCatExist = false;

                for (Gui cat : adminShop.categories) {
                    if (cat.getName().equalsIgnoreCase(name)) {
                        ifCatExist = true;
                        break;
                    }
                }

                if (name != null && !ifCatExist) {
                    Gui gu = EcoGuiFactory.createSubGui(GuiInvRow.ROW6, name, displayname, adminShop.shop, null, itemHolder.getType(), itemHolder.getDurability(), displayname);
                    if (gu != null) {
                        Dao_Categorie prepareSqlCategorie = new Dao_Categorie(name, displayname, itemHolder.getType().toString(), itemHolder.getDurability());

                        Request.addCategorie(prepareSqlCategorie);
                        adminShop.categories.add(gu);

                        String successMessage = ChatFormatting.formatText(adminShop.getMessage().getCustomConfig().getString("prefix"));

                        successMessage += adminShop.getMessage().getCustomConfig().getString("success_add_categorie");

                        successMessage = successMessage.replace("{CAT_NAME}", gu.getName());
                        sender.sendMessage(ChatFormatting.formatText(successMessage));
                    }
                } else {
                    sender.sendMessage(ChatFormatting.formatText(adminShop.getMessage().getCustomConfig().getString("prefix")) +
                            ChatFormatting.formatText(adminShop.getMessage().getCustomConfig().getString("error_add_categorie")));
                }
            }
        } else {
            this.command_help(sender);
        }
        return true;
    }

    /**
     * @param sender Command Sender
     * @param args   Command arguments
     * @return if command is correctly executed
     */
    private boolean addItemIntoCategorie(CommandSender sender, String[] args) {
        if (!sender.hasPermission("adminshop.item.add")) {
            sender.sendMessage(ChatFormatting.formatText(adminShop.getMessage().getCustomConfig().getString("prefix")) +
                    ChatFormatting.formatText(adminShop.getMessage().getCustomConfig().getString("permission_deny")));
            return true;
        }
        if (args.length == 6) {
            ItemStack itemHolder = ((Player) sender).getInventory().getItemInMainHand();
            // sender.getServer().getPlayer(sender.getName()).getOpenInventory().getBottomInventory().remove(itemHolder);
            if (itemHolder.getType() != Material.AIR) {
                String catname = (!args[2].contains("&")) ? args[2] : null;

                Double buy_price;
                Double sell_price;
                try {
                    buy_price = Double.valueOf(args[3]);
                    sell_price = Double.valueOf(args[4]);
                } catch (NumberFormatException e) {
                    String errorPrice = adminShop.getMessage().getCustomConfig().getString("prefix");
                    errorPrice += adminShop.getMessage().getCustomConfig().getString("error_add_item");
                    errorPrice = errorPrice.replace("{ITEM}", itemHolder.getType().toString().toLowerCase());
                    errorPrice = errorPrice.replace("{CAT}", catname);
                    errorPrice += adminShop.getMessage().getCustomConfig().getString("add_item_buysell_error");
                    sender.sendMessage(ChatFormatting.formatText(errorPrice));
                    return false;
                }
                ItemStatut statut = ItemStatut.getStatut(args[5]);
                if (statut == null) {

                    String errorStatut = adminShop.getMessage().getCustomConfig().getString("prefix");
                    errorStatut += adminShop.getMessage().getCustomConfig().getString("error_add_item");
                    errorStatut = errorStatut.replace("{ITEM}", itemHolder.getType().toString().toLowerCase());
                    errorStatut = errorStatut.replace("{CAT}", catname);
                    errorStatut += adminShop.getMessage().getCustomConfig().getString("add_item_wrong_statut");
                    sender.sendMessage(ChatFormatting.formatText(errorStatut));
                    return false;
                }
                if (catname != null) {
                    for (int i = 0; i < adminShop.categories.size(); i++) {
                        if (adminShop.categories.get(i).getName().equalsIgnoreCase(catname)) {
                            adminShop.categories.get(i).addItem(new EcoItem(adminShop.categories.get(i), itemHolder.getType(), 1, (short) itemHolder.getDurability(), buy_price, sell_price, statut));
                            break;
                        }
                    }

                    Dao_item sqlitem = new Dao_item(itemHolder.getType().toString(), itemHolder.getDurability(), buy_price, sell_price, statut.getName());

                    Request.addItemIntoCategorie(catname, sqlitem);

                    String addSuccess = adminShop.getMessage().getCustomConfig().getString("prefix");
                    addSuccess += adminShop.getMessage().getCustomConfig().getString("success_add_item");
                    addSuccess = addSuccess.replace("{ITEM}", itemHolder.getType().toString().toLowerCase());
                    addSuccess = addSuccess.replace("{CAT}", catname);
                    sender.sendMessage(ChatFormatting.formatText(addSuccess));
                }
            }
        }
        return true;
    }

    /**
     * @param sender Command Sender
     * @return if command is correctly executed
     */
    private boolean toggleEditMode(CommandSender sender) {
        if (!sender.hasPermission("adminshop.edit")) {
            sender.sendMessage(ChatFormatting.formatText(adminShop.getMessage().getCustomConfig().getString("prefix")) +
                    ChatFormatting.formatText(adminShop.getMessage().getCustomConfig().getString("permission_deny")));
            return true;
        }
        if (!AdminShop.playerIsEditorMode(((Player) sender))) {
            AdminShop.setPlayerEditionMode(((Player) sender));
            sender.sendMessage(ChatFormatting.formatText(adminShop.getMessage().getCustomConfig().getString("editor_mode_enable")));
        } else {
            AdminShop.removePlayerEditionMode(((Player) sender));
            sender.sendMessage(ChatFormatting.formatText(adminShop.getMessage().getCustomConfig().getString("editor_mode_disable")));
        }
        return true;
    }

    /**
     * @param sender Command Sender
     * @return if command is correctly executed
     */
    private boolean reload(CommandSender sender) {
        if (!sender.hasPermission("adminshop.reload")) {
            sender.sendMessage(ChatFormatting.formatText(adminShop.getMessage().getCustomConfig().getString("prefix")) +
                    ChatFormatting.formatText(adminShop.getMessage().getCustomConfig().getString("permission_deny")));
            return true;
        }
        Runnable reloadConfig = () -> {
            //Reload config file
            adminShop.getConf().reloadCustomConfig();
            adminShop.getMessage().reloadCustomConfig();

            //Reload database

            if (Dao.getInstance() != null) {
                Dao.getInstance().closeConnection();
            }

            Dao.getInstance().setBdd_address(adminShop.getConf().getCustomConfig().getString("Database.address"));
            Dao.getInstance().setBdd_port(adminShop.getConf().getCustomConfig().getInt("Database.port"));
            Dao.getInstance().setBdd_name(adminShop.getConf().getCustomConfig().getString("Database.name"));
            Dao.getInstance().setBdd_username(adminShop.getConf().getCustomConfig().getString("Database.username"));
            Dao.getInstance().setBdd_password(adminShop.getConf().getCustomConfig().getString("Database.password"));

            Dao.getInstance().connection();
            if (Dao.getInstance().getBdd_connection() == null) {
                sender.sendMessage(this.prefix + ChatFormatting.formatText(adminShop.getMessage().getCustomConfig().getString("plugin_reload_failure")));
            } else {
                adminShop.shop.exitAll();
                for (Gui cat : adminShop.categories) {
                    cat.exitAll();
                }
                GuiManager.getGuis().clear();
                adminShop.shop = new Gui(GuiInvRow.ROW3, "adminshop", "&4AdminShop &f- &eCategories");
                adminShop.categories = new ArrayList<>();

                adminShop.initAdminShopGui();

                sender.sendMessage(this.prefix + ChatFormatting.formatText(adminShop.getMessage().getCustomConfig().getString("plugin_reload_success")));
            }
        };

        new Thread(reloadConfig).start();

        return true;
    }

    private boolean total(CommandSender sender, String[] args){
        if (!sender.hasPermission("adminshop.total")) {
            sender.sendMessage(ChatFormatting.formatText(adminShop.getMessage().getCustomConfig().getString("prefix")) +
                    ChatFormatting.formatText(adminShop.getMessage().getCustomConfig().getString("permission_deny")));
            return true;
        }

        if(args.length == 2){
            if(args[1].equalsIgnoreCase("out")){
                Double in = Request.getTotalEco("BUY");

                String in_msg = adminShop.getMessage().getCustomConfig().getString("prefix");
                in_msg += adminShop.getMessage().getCustomConfig().getString("total_buy");

                in_msg = in_msg.replace("{OUT}", String.valueOf(in));

                sender.sendMessage(ChatFormatting.formatText(in_msg));


            }else if(args[1].equalsIgnoreCase("in")){
                Double out = Request.getTotalEco("SELL");
                String out_msg = adminShop.getMessage().getCustomConfig().getString("prefix");
                out_msg += adminShop.getMessage().getCustomConfig().getString("total_sell");

                out_msg = out_msg.replace("{IN}", String.valueOf(out));

                sender.sendMessage(ChatFormatting.formatText(out_msg));

            }
        }



        return true;
    }
}

