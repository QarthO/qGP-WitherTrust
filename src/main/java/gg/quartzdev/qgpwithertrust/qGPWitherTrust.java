package gg.quartzdev.qgpwithertrust;

import gg.quartzdev.qgpwithertrust.listeners.WitherBlockBreak;
import gg.quartzdev.qgpwithertrust.listeners.WitherHurtEntity;
import gg.quartzdev.qgpwithertrust.listeners.WitherSpawnListener;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public final class qGPWitherTrust extends JavaPlugin {

    private static qGPWitherTrust instance;

    public static qGPWitherTrust getInstance(){
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        // Plugin startup logic
        int pluginId = 20410;
        Metrics metrics = new Metrics(this, pluginId);

        this.getServer().getPluginManager().registerEvents(new WitherSpawnListener(), this);
        this.getServer().getPluginManager().registerEvents(new WitherHurtEntity(), this);
        this.getServer().getPluginManager().registerEvents(new WitherBlockBreak(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
