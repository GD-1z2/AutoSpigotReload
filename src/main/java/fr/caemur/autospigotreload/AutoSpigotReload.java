package fr.caemur.autospigotreload;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;
import java.util.List;

public class AutoSpigotReload extends JavaPlugin {
    private WatchServicesManager watchServicesManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        watchServicesManager = new WatchServicesManager(this);

        final PluginCommand command = getCommand("autospigotreload");

        if (command == null) {
            System.out.println("Failed to get command autospigotreload");

            return;
        }

        command.setExecutor(new AsrCommand(this));
        command.setTabCompleter(new AsrCompleter(this));
    }

    @Override
    public void onDisable() {
        final Object filesConfig = getConfig().get("files");

        if (filesConfig instanceof List) {
            final List<String> files = (List<String>) filesConfig;

            files.clear();

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
