package plugin.scripts.core;

import org.bukkit.command.*;
import plugin.scripts.storage.AdvancementStorage;

import java.util.List;

public class ShowRewardTab implements TabCompleter {

    @Override
    public List<String> onTabComplete(
            CommandSender sender,
            Command command,
            String alias,
            String[] args
    ) {

        if (args.length == 1) {
            return AdvancementStorage.getAll()
                    .keySet()
                    .stream()
                    .filter(a -> a.startsWith(args[0]))
                    .toList();
        }

        return List.of();
    }
}