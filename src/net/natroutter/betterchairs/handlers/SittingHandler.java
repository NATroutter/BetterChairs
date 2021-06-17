package net.natroutter.betterchairs.handlers;

import net.natroutter.betterchairs.BetterChairs;
import net.natroutter.betterchairs.utilities.Config;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.UUID;

public class SittingHandler implements Listener {

    private final Config config = BetterChairs.getConf();

    @EventHandler
    public void onInteractBlock(PlayerInteractEvent e) {
        if (!e.hasBlock()) { return; }
        Block block = e.getClickedBlock();
        Player p = e.getPlayer();

        if (block.getType().name().endsWith("_SLAB") || block.getType().name().endsWith("_STAIRS")) {
            if (!ChairHandler.isChair(block)) {return;}
            if (p.isInsideVehicle()) {return;}
            if (p.isSneaking()) {return;}

            if (p.hasPermission("betterchairs.sit")) {
                sitOnBlock(p, block);
            } else {
                if (config.useMessages) {
                    p.sendMessage(config.prefix + config.noperm);
                }
            }
        }
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
        chair.setGravity(false);
        chair.setInvulnerable(true);
        chair.setCustomNameVisible(false);
        chair.setCustomName("BetterChairs--" + UUID.randomUUID());
        chair.setInvisible(true);
        chair.addPassenger(p);

        if (config.useMessages) {
            p.sendMessage(config.prefix + config.Sitting);
        }
    }



}
