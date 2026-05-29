package plugin.scripts;

import org.bukkit.plugin.java.JavaPlugin;
import plugin.scripts.commands.CommandsManager;
import plugin.scripts.core.GhostManager;
import plugin.scripts.core.PointsManager;
import plugin.scripts.core.ShowRewardTab;
import plugin.scripts.listeners.DeathListener;
import plugin.scripts.storage.StorageManager;
import plugin.scripts.tab.TabManager;

import java.util.Objects;

public class ThePartOfLife extends JavaPlugin {

    private static ThePartOfLife instance;

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new DeathListener(), this);

        TabManager tabManager = new TabManager();
        getServer().getPluginManager().registerEvents(new GhostManager(), this);

        getServer().getPluginManager().registerEvents(tabManager, this);
        tabManager.startUpdater();

        new StorageManager(this).loadRewards();

        CommandsManager commandsManager = new CommandsManager();


        Objects.requireNonNull(getCommand("addhealth")).setExecutor(commandsManager);
        Objects.requireNonNull(getCommand("sethealth")).setExecutor(commandsManager);
        Objects.requireNonNull(getCommand("realkill")).setExecutor(commandsManager);
        Objects.requireNonNull(getCommand("respawn")).setExecutor(commandsManager);
        Objects.requireNonNull(getCommand("showreward")).setExecutor(new PointsManager());
        Objects.requireNonNull(getCommand("showreward")).setTabCompleter(new ShowRewardTab());
    }

    public static ThePartOfLife getInstance() {
        return instance;
    }
}