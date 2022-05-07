package net.natroutter.betterchairs.commands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.natroutter.betterchairs.BetterChairs;
import net.natroutter.betterchairs.handlers.ChairHandler;
import net.natroutter.betterchairs.utilities.Config;
import net.natroutter.natlibs.utilities.StringHandler;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BetterChairsCMD extends Command {

    private ChairHandler chairHandler;
    private BetterChairs chairs;
    private Config config;

    public BetterChairsCMD(BetterChairs chairs) {
        super("BetterChairs");
        this.setAliases(Collections.singletonList("bc"));
        this.chairs = chairs;
        this.config = chairs.getConf();
        this.chairHandler = chairs.getChairHandler();
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(config.prefix + config.onlyingame);
            return false;
        }
        Player p = (Player)sender;

        if (args.length == 0) {
            p.sendMessage(" ");
            p.sendMessage("§8§l§m━━━━━━━━━━━━§8§l|§9§l BetterChairs §8§l|§m━━━━━━━━━━━━");
            p.sendMessage("§8§l» §7BetterChairs version §b" + chairs.getPlugin().getDescription().getVersion());
            p.sendMessage("§8§l» §7Made by: §bNATroutter");
            p.sendMessage("§8§l» §7Website: §bhttps://NATroutter.net");
            p.sendMessage("§8§l§m━━━━━━━━━━━━§8§l|§9§l BetterChairs §8§l|§m━━━━━━━━━━━━");
            p.sendMessage(" ");
        } else if (args.length == 1) {

            if (args[0].equalsIgnoreCase("help")) {
                if (p.hasPermission("betterchars.help")) {
                    for (String line : config.helpMessage) {
                        p.sendMessage(line);
                    }
                } else {
                    p.sendMessage(config.prefix + config.noperm);
                }
            } else if (args[0].equalsIgnoreCase("add")) {

                if (p.hasPermission("betterchars.add")) {

                    Block target = p.getTargetBlockExact(3, FluidCollisionMode.NEVER);
                    if (target == null) {
                        p.sendMessage(config.prefix + config.InvalidTarget);
                        return false;
                    }
                    if (target.getType().name().endsWith("_SLAB") || target.getType().name().endsWith("_STAIRS")) {
                        chairHandler.addChair(target);
                        p.sendMessage(config.prefix + config.ChairAdded);

                    } else {
                        p.sendMessage(config.prefix + config.InvalidTarget);
                    }
                } else {
                    p.sendMessage(config.prefix + config.noperm);
                }
            } else if (args[0].equalsIgnoreCase("remove")) {

                if (p.hasPermission("betterchars.remove")) {

                    Block target = p.getTargetBlockExact(3, FluidCollisionMode.NEVER);
                    if (target == null) {
                        p.sendMessage(config.prefix + config.InvalidTarget);
                        return false;
                    }
                    if (target.getType().name().endsWith("_SLAB") || target.getType().name().endsWith("_STAIRS")) {
                        chairHandler.removeChair(target);
                        p.sendMessage(config.prefix + config.ChairRemoved);

                    } else {
                        p.sendMessage(config.prefix + config.InvalidTarget);
                    }
                } else {
                    p.sendMessage(config.prefix + config.noperm);
                }
            } else if (args[0].equalsIgnoreCase("list")) {

                if (p.hasPermission("betterchars.list")) {

                    List<Location> locs = chairHandler.getList();
                    if (locs == null) {
                        p.sendMessage(config.prefix + config.NoChairs);
                        return false;
                    }

                    p.sendMessage(config.cfm.header);
                    for (int i = 0; i<locs.size();i++) {
                        Location loc = locs.get(i);

                        StringHandler line = new StringHandler(config.cfm.entry);
                        line.replaceAll("{num}" , i);
                        line.replaceAll("{x}", loc.getBlockX());
                        line.replaceAll("{y}", loc.getBlockY());
                        line.replaceAll("{z}", loc.getBlockZ());
                        TextComponent comp = new TextComponent(line.build());

                        TextComponent tp_btn = new TextComponent(config.cfm.teleport_btn);
                        tp_btn.setClickEvent(new ClickEvent(
                                ClickEvent.Action.RUN_COMMAND,
                                "/minecraft:tp " + p.getName() + " " + loc.getBlockX() + " " + (loc.getBlockY() + 1) + " " + loc.getBlockZ())
                        );
                        tp_btn.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(config.cfm.teleport_hint)));
                        comp.addExtra(tp_btn);
                        p.spigot().sendMessage(comp);

                    }
                    p.sendMessage(config.cfm.footer);

                } else {
                    p.sendMessage(config.prefix + config.noperm);
                }
            } else {
                p.sendMessage(config.prefix + config.invalidargs);
            }
        } else {
            p.sendMessage(config.prefix + config.toomanyargs);
        }

        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {

        if (args.length == 1) {

            ArrayList<String> firstArgs = new ArrayList<>();
            if (sender.hasPermission("betterchars.add")) {
                firstArgs.add("add");
            }
            if (sender.hasPermission("betterchars.remove")) {
                firstArgs.add("remove");
            }
            if (sender.hasPermission("betterchars.list")) {
                firstArgs.add("list");
            }

            List<String> shorted = new ArrayList<>();
            StringUtil.copyPartialMatches(args[0], firstArgs, shorted);
            Collections.sort(shorted);
            return shorted;
        }
        return null;
    }

}

