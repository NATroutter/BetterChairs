package net.natroutter.betterchairs;

import lombok.Getter;
import net.natroutter.betterchairs.commands.BetterChairsCMD;
import net.natroutter.betterchairs.handlers.ChairHandler;
import net.natroutter.betterchairs.handlers.SittingHandler;
import net.natroutter.betterchairs.utilities.Config;
import net.natroutter.natlibs.NATLibs;
import net.natroutter.natlibs.handlers.Database.YamlDatabase;
import net.natroutter.natlibs.handlers.configuration.ConfigManager;
import net.natroutter.natlibs.utilities.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class BetterChairs extends JavaPlugin {

    private JavaPlugin plugin;
    private Config conf;
    private YamlDatabase database;
    private Utilities utilities;
    private ChairHandler chairHandler;

    @Override
    public void onEnable() {
        plugin = this;

        conf = new ConfigManager(this).load(Config.class);

        database = new YamlDatabase(this);
        utilities = new Utilities(this);
        chairHandler = new ChairHandler(this);

        CommandMap map = Bukkit.getCommandMap();
        PluginManager pm = Bukkit.getPluginManager();

        map.register("betterChairs", new BetterChairsCMD(this));

        pm.registerEvents(new SittingHandler(this), this);

        chairHandler.load();
    }

    @Override
    public void onDisable() {
        if (chairHandler == null) {return;}
        chairHandler.unload();
    }
}
