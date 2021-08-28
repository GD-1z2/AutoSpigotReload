package fr.caemur.autospigotreload;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WatchServicesManager {
    private final JavaPlugin plugin;

    private final Map<Path, WatchService> watchServices;

    public WatchServicesManager(JavaPlugin plugin) {
        this.plugin = plugin;

        watchServices = new HashMap<>();

        for (String path : (List<String>) plugin.getConfig().get("files")) {
            try {
                addWatcher(Paths.get(path));
            } catch (FileNotFoundException e) {
                System.out.println("The file " + path + " was not found and couldn't be added to watch list");
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        for (WatchService watchService : watchServices.values()) {
            try {
                watchService.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void enable() {
        plugin.getConfig().set("enabled", true);
    }

    public void disable() {
        plugin.getConfig().set("enabled", false);
    }

    public Map<Path, WatchService> getWatchServices() {
        return watchServices;
    }

    public boolean addWatcher(Path path) throws FileNotFoundException {
        final WatchService watchService;

        if (!path.toFile().isFile())
            throw new FileNotFoundException();

        try {
            watchService = FileSystems.getDefault().newWatchService();
            WatchKey watchKey = path.getParent().register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
        } catch (IOException e) {
            Bukkit.getLogger().severe("Failed to create watch service");
            e.printStackTrace();

            return false;
        }

        watchServices.put(path, watchService);

        Bukkit.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            WatchKey key;
            while (true) {
                try {
                    if ((key = watchService.take()) == null) break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    continue;
                } catch (ClosedWatchServiceException e) {
                    return;
                }
                for (WatchEvent<?> event : key.pollEvents()) {
                    if ((boolean) plugin.getConfig().get("enabled") && path.getFileName().compareTo((Path) event.context()) == 0) {
                        Bukkit.getScheduler().runTask(plugin, () -> {
                            Bukkit.reload();
                            Bukkit.broadcastMessage("§6Reloaded because of " + path.getFileName());
                        });
                    }
                }
                key.reset();
            }
        });

        return true;
    }

    public boolean removeWatcher(Path file) {
        if (!watchServices.containsKey(file))
            return false;

        try {
            watchServices.get(file).close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        watchServices.remove(file);

        return true;
    }

    public int clear() {
        final int total = watchServices.size();

        for (WatchService watchService : watchServices.values()) {
            try {
                watchService.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        watchServices.clear();

        return total;
    }
}
