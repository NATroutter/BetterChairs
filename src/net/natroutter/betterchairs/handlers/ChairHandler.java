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

    private final YamlDatabase database ;
    private final Utilities util;

    public ChairHandler(BetterChairs chairs) {
        database = chairs.getDatabase();
        util = chairs.getUtilities();
    }

    private List<Location> Chairs = new ArrayList<>();

    public void load() {
        wipe();
        List<Location> list = getList();
        if (list != null) {
            Chairs = getList();
        }
    }
    public void unload() {
        wipe();
        saveLocs();
    }

    public void wipe() {
        for (World w : Bukkit.getServer().getWorlds()) {
            for (Entity ent : w.getEntities()) {
                if (ent instanceof ArmorStand chair) {
                    if (chair.getCustomName() == null) {continue;}
                    if (chair.getCustomName().startsWith("BetterChairs--")) {
                        ent.remove();
                    }
                }
            }
        }
    }


    public boolean isChair(Block block) {
        for (Location loc : Chairs) {
            if (locMatch(loc, block.getLocation())) {
                return true;
            }
        }
        return false;
    }

    public void addChair(Block block) {
        Chairs.add(block.getLocation());
        saveLocs();
    }

    public void removeChair(Block block) {
        for (int i = 0; i < Chairs.size(); i++) {
            Location loc = Chairs.get(i);
            if (locMatch(loc, block.getLocation())) {
                Chairs.remove(i);
            }
        }
        saveLocs();
    }


    private boolean locMatch(Location loc1, Location loc2) {
        return loc1.getBlockX() == loc2.getBlockX() && loc1.getBlockY() == loc2.getBlockY() && loc1.getBlockZ() == loc2.getBlockZ();
    }


    private Character separator = '\\';
    private void saveLocs() {
        List<String> list = new ArrayList<>();
        for (Location ob : Chairs) {
            list.add(util.serializeLocation(ob, separator));
        }
        database.save("General", "Chairs", list);
    }

    public List<Location> getList() {
        List<?> oldlist = database.getList("General", "Chairs");
        if (oldlist == null ) {return null;}

        List<Location> newlist = new ArrayList<>();
        for (Object ob : oldlist) {
            newlist.add(util.deserializeLocation(ob.toString(), separator));
        }
        return newlist;
    }


}
