package fr.caemur.autospigotreload;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AsrCommand implements CommandExecutor {
    private AutoSpigotReload plugin;

    public AsrCommand(AutoSpigotReload plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            switch (args[0]) {
                case "on":
                    plugin.getWatchServicesManager().enable();
                    sender.sendMessage("§aEnabled autoreload");
                    return true;

                case "off":
                    plugin.getWatchServicesManager().disable();
                    sender.sendMessage("§cDisabled autoreload");
                    return true;

                case "list":
                    for (Path path : plugin.getWatchServicesManager().getWatchServices().keySet()) {
                        sender.sendMessage(" - " + path);
                    }

                    sender.sendMessage("§7There are currently " + plugin.getWatchServicesManager().getWatchServices().size() + " watched files");
                    if (!(boolean) plugin.getConfig().get("enabled"))
                        sender.sendMessage("§7The plugin is currently §cdisabled");

                    return true;

                case "add":
                    if (args.length > 1) {
                        final Path newPath;
                        try {
                            newPath = Paths.get(args[1]);
                        } catch (InvalidPathException e) {
                            sender.sendMessage("§cInvalid path");
                            return false;
                        }

                        if (!plugin.getWatchServicesManager().getWatchServices().containsKey(newPath)) {
                            sender.sendMessage(plugin.getWatchServicesManager().addWatcher(newPath)
                                    ? "§aThe file " + newPath + " was added !"
                                    : "§cFailed to add the file");
                        } else {
                            sender.sendMessage("§cThis file was already being watched");
                        }
                    } else {
                        sender.sendMessage(new String[]{
                                "§cYou need to provide a file path :",
                                "§7/autospigotreload add ./plugins/awesomeplugin.jar"});

                        return false;
                    }

                    return true;

                case "remove":
                    if (args.length > 1) {
                        final Path path;
                        try {
                            path = Paths.get(args[1]);
                        } catch (InvalidPathException e) {
                            sender.sendMessage("§cInvalid path");
                            return false;
                        }

                        sender.sendMessage(plugin.getWatchServicesManager().removeWatcher(path)
                                ? "§aThe file " + path + " was removed !"
                                : "§cThis file was not found in the list");
                    } else {
                        sender.sendMessage(new String[]{
                                "§cYou need to provide a file path :",
                                "§7/autospigotreload remove /plugins/awesomeplugin.jar"});

                        return false;
                    }

                    return true;

                case "clear":
                    sender.sendMessage("§aRemoved " + plugin.getWatchServicesManager().clear() + " files");
                    return true;
            }
        }

        sender.sendMessage("§cExpected one of [on off list add remove clear] as first argument");

        return false;
    }
}
