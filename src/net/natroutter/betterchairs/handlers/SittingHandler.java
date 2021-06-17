package net.natroutter.betterchairs.handlers;

import net.natroutter.betterchairs.BetterChairs;
import net.natroutter.betterchairs.utilities.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.*;

public class SittingHandler implements Listener {

    private final Config config = BetterChairs.getConf();
    public static HashMap<UUID, Long> interactCooldowns = new HashMap<>();

    @EventHandler
    public void onInteractBlock(PlayerInteractEvent e) {
        if (!e.hasBlock()) { return; }
        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {return;}

        Block block = e.getClickedBlock();
        Player p = e.getPlayer();

        if (block.getType().name().endsWith("_SLAB") || block.getType().name().endsWith("_STAIRS")) {
            long cl = ((interactCooldowns.getOrDefault(p.getUniqueId(), 0L) /1000)+2) - (System.currentTimeMillis()/1000);
            if (cl > 0) { return; }
            interactCooldowns.put(p.getUniqueId(), System.currentTimeMillis());

            if (!ChairHandler.isChair(block)) {return;}
            if (p.isInsideVehicle()) {return;}
            if (p.isSneaking()) {return;}

            if (p.hasPermission("betterchairs.sit")) {
                Location loc = block.getLocation();
                loc.setX(loc.getBlockX() + 0.5);
                loc.setZ(loc.getBlockZ() + 0.5);
                Collection<Entity> ents = p.getWorld().getNearbyEntities(loc.add(0, -1, 0), 0.2, 0.2, 0.2);
                for (Entity ent : ents) {
                    if (ent instanceof ArmorStand) {
                        ArmorStand chair = (ArmorStand)ent;
                        if (ent.getName().startsWith("BetterChairs--")) {
                            return;
                        }
                    }
                }
                sitOnBlock(p, block);
            } else {
                if (config.useMessages) {
                    p.sendMessage(config.prefix + config.noperm);
                }
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (p.isInsideVehicle()) {
            if (p.getVehicle() instanceof ArmorStand) {
                if (p.getVehicle().getName().startsWith("BetterChairs--")) {
                    e.setCancelled(true);
                }
            }
            return;
        }
        if (ChairHandler.isChair(e.getBlock())) {
            Location loc = e.getBlock().getLocation();
            loc.setX(loc.getBlockX() + 0.5);
            loc.setZ(loc.getBlockZ() + 0.5);
            Collection<Entity> ents = p.getWorld().getNearbyEntities(loc.add(0, -1, 0), 0.2, 0.2, 0.2);
            for (Entity ent : ents) {
                if (ent instanceof ArmorStand) {
                    ArmorStand chair = (ArmorStand)ent;

                    if (ent.getName().startsWith("BetterChairs--")) {
                        for (Entity pas : chair.getPassengers()) {
                            pas.eject();
                        }
                        Bukkit.getScheduler().scheduleSyncDelayedTask(BetterChairs.getInstance(), ()->{
                            chair.remove();
                            ChairHandler.removeChair(e.getBlock());
                        }, 3);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        if (p.isInsideVehicle()) {
            if (p.getVehicle() instanceof ArmorStand) {
                ArmorStand chair = (ArmorStand) p.getVehicle();
                if (chair.getCustomName().startsWith("BetterChairs--")) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        interactCooldowns.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onStandup(EntityDismountEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (e.getDismounted() instanceof ArmorStand) {
                ArmorStand chair = (ArmorStand)e.getDismounted();
                if (chair.getCustomName().startsWith("BetterChairs--")) {
                    if (config.useMessages) {
                        p.sendMessage(config.prefix + config.Standup);
                    }
                    chair.remove();
                    Bukkit.getScheduler().scheduleSyncDelayedTask(BetterChairs.getInstance(), ()->{
                        p.teleport(p.getLocation().add(0, 0.5, 0));
                    }, 3);
                }
            }
        }
    }

    private void sitOnBlock(Player p, Block block) {

        Location loc = block.getLocation();
        loc.setX(loc.getBlockX() + 0.5);
        loc.setZ(loc.getBlockZ() + 0.5);
        loc.setY(loc.getBlockY() - 1.2);
        loc.setYaw(90);

        ArmorStand chair = p.getWorld().spawn(loc, ArmorStand.class);
        chair.setInvisible(true);
        chair.setGravity(false);
        chair.setInvulnerable(true);
        chair.setCustomNameVisible(false);
        chair.setCustomName("BetterChairs--" + UUID.randomUUID());
        chair.addPassenger(p);

        if (config.useMessages) {
            p.sendMessage(config.prefix + config.Sitting);
        }
    }



}
