package net.natroutter.betterchairs.handlers;

import net.natroutter.betterchairs.BetterChairs;
import net.natroutter.natlibs.handlers.Database.YamlDatabase;
import net.natroutter.natlibs.utilities.Utilities;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class ChairHandler {

    private static final YamlDatabase database = BetterChairs.getDatabase();
    private static final Utilities util = BetterChairs.getUtilities();

    private static List<Location> Chairs = new ArrayList<>();

    public static void load() {
        List<Location> list = getList();
        if (list != null) {
            Chairs = getList();
        }
    }
    public static void unload() { saveLocs(); }



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
