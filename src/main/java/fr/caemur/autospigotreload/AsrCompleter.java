package fr.caemur.autospigotreload;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AsrCompleter implements TabCompleter {
    private final AutoSpigotReload plugin;

    public AsrCompleter(AutoSpigotReload plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("list", "add", "remove", "clear", "on", "off");

        } else if (args.length == 2 && args[0].equals("remove")) {
            final List<String> completions = new ArrayList<>();

            for (Path path : plugin.getWatchServicesManager().getWatchServices().keySet()) {
                completions.add(path.toString());
            }

            return completions;

        } else {
            return Collections.emptyList();
        }
    }
}
