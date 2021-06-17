package net.natroutter.betterchairs.utilities;

import java.util.ArrayList;

public class Config {

    public boolean useMessages = false;

    public String prefix = "§9§lBetterChairs §8§l» ";
    public String onlyingame = "§7This command can only be used ingame!";
    public String noperm = "§7You do not have permissions this this!";
    public String invalidargs = "§7Invalid command arguments!";
    public String toomanyargs = "§7Toomany command arguments!";
    public String InvalidTarget = "§7Invalid target!";
    public String Standup = "§7You are now standing!";
    public String Sitting = "§7You are now Sitting!";
    public String ChairAdded = "§7Chair added!";
    public String ChairRemoved = "§7Chair removed!";
    public String NoChairs = "§7There are not chairs!";

    public ChairListFormat cfm = new ChairListFormat();
    public class ChairListFormat {
        public String header = "§8§l§m━━━━━━━━━━━━§8§l|§9§l BetterChairs §8§l|§m━━━━━━━━━━━━";
        public String footer = "§8§l§m━━━━━━━━━━━━§8§l|§9§l BetterChairs §8§l|§m━━━━━━━━━━━━";
        public String entry = "§b{num}§7. §8(§b{x}§7, §b{y}§7, §b{z}§8) ";
        public String teleport_btn = "§8[§bTELEPORT§8]";
        public String teleport_hint = "§bClick here to teleport!";
    }

    public ArrayList<String> helpMessage = new ArrayList<>(){{
        add(" ");
        add("§8§l§m━━━━━━━━━━━━§8§l|§9§l BetterChairs §8§l|§m━━━━━━━━━━━━");
        add(" ");
        add("§8§l» §b/bc add §8|| §7Add targeted block to chairs");
        add("§8§l» §b/bc remove §8|| §7Remove targeted block from chairs");
        add("§8§l» §b/bc list §8|| §7 Show list of chairs");
        add(" ");
        add("§8§l§m━━━━━━━━━━━━§8§l|§9§l BetterChairs §8§l|§m━━━━━━━━━━━━");
        add(" ");
    }};

}
