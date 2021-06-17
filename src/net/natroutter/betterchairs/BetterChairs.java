package net.natroutter.betterchairs;

import net.natroutter.betterchairs.commands.BetterChairsCMD;
import net.natroutter.betterchairs.handlers.ChairHandler;
import net.natroutter.betterchairs.handlers.SittingHandler;
import net.natroutter.betterchairs.utilities.Config;
import net.natroutter.natlibs.NATLibs;
import net.natroutter.natlibs.handlers.Database.YamlDatabase;
import net.natroutter.natlibs.handlers.EventManager;
import net.natroutter.natlibs.handlers.FileManager;
import net.natroutter.natlibs.objects.ConfType;
import net.natroutter.natlibs.utilities.Utilities;
import org.bukkit.plugin.java.JavaPlugin;

public class BetterChairs extends JavaPlugin {

    private static JavaPlugin plugin;
    private static Config config;
    private static YamlDatabase database;
    private static Utilities utilities;

    public static JavaPlugin getInstance() { return plugin; }
    public static Config getConf() {return config;}
    public static YamlDatabase getDatabase() {return database;}
    public static Utilities getUtilities() {return utilities;}


    @Override
    public void onEnable() {
        plugin = this;
        new NATLibs(this);

        FileManager cfgLoader = new FileManager(this, ConfType.Config);
        config = cfgLoader.load(Config.class);

        database = new YamlDatabase(this);
        utilities = new Utilities(this);

        EventManager evm = new EventManager(this);
        evm.RegisterCommands(BetterChairsCMD.class);
        evm.RegisterListeners(SittingHandler.class);

        ChairHandler.load();
    }

    @Override
    public void onDisable() {
        ChairHandler.unload();
    }
}
