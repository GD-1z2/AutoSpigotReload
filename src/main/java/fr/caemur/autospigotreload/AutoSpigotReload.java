package fr.caemur.autospigotreload;

import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;
import java.util.List;

public class AutoSpigotReload extends JavaPlugin {
    private WatchServicesManager watchServicesManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        watchServicesManager = new WatchServicesManager(this);

        getCommand("autospigotreload").setExecutor(new AsrCommand(this));
    }

    @Override
    public void onDisable() {
        final Object filesConfig = getConfig().get("files");

        if (filesConfig instanceof List) {
            final List<String> files = (List<String>) filesConfig;

            for (Path path : watchServicesManager.getWatchServices().keySet()) {
                files.add(path.toString());
            }
        }

        saveConfig();

        watchServicesManager.stop();
    }

    public WatchServicesManager getWatchServicesManager() {
        return watchServicesManager;
    }
}
