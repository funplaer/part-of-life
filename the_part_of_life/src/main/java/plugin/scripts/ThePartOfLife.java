package plugin.scripts;

import org.bukkit.plugin.java.JavaPlugin;
import plugin.scripts.listeners.DeathListener;
import plugin.scripts.tab.TabManager;

public class ThePartOfLife extends JavaPlugin {

    private static ThePartOfLife instance;

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new DeathListener(), this);

        TabManager tabManager = new TabManager();
        getServer().getPluginManager().registerEvents(tabManager, this);
        tabManager.startUpdater();
    }

    public static ThePartOfLife getInstance() {
        return instance;
    }
}