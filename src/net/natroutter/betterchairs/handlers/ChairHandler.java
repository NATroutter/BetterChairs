package net.natroutter.betterchairs.handlers;

import net.natroutter.betterchairs.BetterChairs;
import net.natroutter.natlibs.handlers.Database.YamlDatabase;
import net.natroutter.natlibs.utilities.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ChairHandler {

    private static final YamlDatabase database = BetterChairs.getDatabase();
    private static final Utilities util = BetterChairs.getUtilities();

    private static List<Location> Chairs = new ArrayList<>();

    public static void load() {
        wipe();
        List<Location> list = getList();
        if (list != null) {
            Chairs = getList();
        }
    }
    public static void unload() {
        wipe();
        saveLocs();
    }

    public static void wipe() {
        for (World w : Bukkit.getServer().getWorlds()) {
            for (Entity ent : w.getEntities()) {
                if (ent instanceof ArmorStand) {
                    ArmorStand chair = (ArmorStand)ent;
                    if (chair.getCustomName() == null) {continue;}
                    if (chair.getCustomName().startsWith("BetterChairs--")) {
                        ent.remove();
                    }
                }
            }
        }
    }


    public static boolean isChair(Block block) {
        for (Location loc : Chairs) {
            if (locMatch(loc, block.getLocation())) {
                return true;
            }
        }
        return false;
    }

    public static void addChair(Block block) {
        Chairs.add(block.getLocation());
        saveLocs();
    }

    public static void removeChair(Block block) {
        for (int i = 0; i < Chairs.size(); i++) {
            Location loc = Chairs.get(i);
            if (locMatch(loc, block.getLocation())) {
                Chairs.remove(i);
            }
        }
        saveLocs();
    }


    private static boolean locMatch(Location loc1, Location loc2) {
        return loc1.getBlockX() == loc2.getBlockX() && loc1.getBlockY() == loc2.getBlockY() && loc1.getBlockZ() == loc2.getBlockZ();
    }

    private static Location pruneLoc(Location loc) {
        return new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), loc.getYaw(), loc.getPitch());
    }

    private static Character separator = '\\';
    private static void saveLocs() {
        List<String> list = new ArrayList<>();
        for (Location ob : Chairs) {
            list.add(util.serializeLocation(ob, separator));
        }
        database.save("General", "Chairs", list);
    }

    public static List<Location> getList() {
        List<?> oldlist = database.getList("General", "Chairs");
        if (oldlist == null ) {return null;}

        List<Location> newlist = new ArrayList<>();
        for (Object ob : oldlist) {
            newlist.add(util.deserializeLocation(ob.toString(), separator));
        }
        return newlist;
    }


}
