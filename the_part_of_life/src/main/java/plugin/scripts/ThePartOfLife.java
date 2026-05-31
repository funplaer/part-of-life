package plugin.scripts;

import org.bukkit.plugin.java.JavaPlugin;
import plugin.scripts.commands.*;
import plugin.scripts.config.ConfigManager;
import plugin.scripts.core.GhostManager;

import plugin.scripts.core.HealingManager;
import plugin.scripts.core.RebornManager;
import plugin.scripts.core.ShowRewardTab;
import plugin.scripts.listeners.AdvancementListener;
import plugin.scripts.listeners.DeathListener;
import plugin.scripts.storage.PlayerPointsManager;
import plugin.scripts.storage.PointLogs;
import plugin.scripts.storage.StorageManager;
import plugin.scripts.tab.TabManager;

import java.util.Objects;

public class ThePartOfLife extends JavaPlugin {

    private static ThePartOfLife instance;
    private PlayerPointsManager pointsManager;
    private ConfigManager configManager;
    public ConfigManager getConfigManager() {
        return configManager;
    }

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

        getServer().getPluginManager().registerEvents(
                new AdvancementListener(),
                this
        );
        pointsManager = new PlayerPointsManager(this);
        pointsManager.load();
        configManager = new ConfigManager(this);
        configManager.load();
        PointLogs.initialize(this);



        Objects.requireNonNull(getCommand("addhealth")).setExecutor(commandsManager);
        Objects.requireNonNull(getCommand("sethealth")).setExecutor(commandsManager);
        Objects.requireNonNull(getCommand("realkill")).setExecutor(commandsManager);
        Objects.requireNonNull(getCommand("respawn")).setExecutor(commandsManager);
        Objects.requireNonNull(getCommand("showreward")).setExecutor(new PointsCommand());
        Objects.requireNonNull(getCommand("showreward")).setTabCompleter(new ShowRewardTab());
        Objects.requireNonNull(getCommand("setpoints")).setExecutor(new SetPointsCommand());
        Objects.requireNonNull(getCommand("addpoints")).setExecutor(new AddPointsCommand());
        Objects.requireNonNull(getCommand("send")).setExecutor(new SendCommand());
        Objects.requireNonNull(getCommand("ptconfig")).setExecutor(new ConfigCommand());
        Objects.requireNonNull(getCommand("buyhealth")).setExecutor(new HealingManager());
        Objects.requireNonNull(getCommand("ptconfig")).setTabCompleter(new ConfigTabCompleter());
        Objects.requireNonNull(getCommand("reborn")).setExecutor(new RebornManager());
    }

    public static ThePartOfLife getInstance() {
        return instance;
    }
    @Override
    public void onDisable() {

        if (pointsManager != null) {
            pointsManager.save();
        }
    }
}