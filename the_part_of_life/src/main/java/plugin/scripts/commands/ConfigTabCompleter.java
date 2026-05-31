package plugin.scripts.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import plugin.scripts.ThePartOfLife;

import java.util.ArrayList;
import java.util.List;

public class ConfigTabCompleter

        implements TabCompleter {


    @Override
    public List<String> onTabComplete(
            CommandSender sender,
            Command command,
            String alias,
            String[] args
    ) {

        List<String> result =
                new ArrayList<>();

        if (args.length == 1) {

            return new ArrayList<>(
                    ThePartOfLife.getInstance()
                            .getConfigManager()
                            .getOptionNames()
            );
        }

        return result;
    }

}