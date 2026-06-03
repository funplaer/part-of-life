package plugin.scripts.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import plugin.scripts.LanguageManager;
import plugin.scripts.storage.AdvancementStorage;

public class PointsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (args.length != 1) {
            sender.sendMessage(
                    "§c"+ LanguageManager.get(
                            "usage_showreward"
                    )
            );
            return true;
        }

        Integer reward =
                AdvancementStorage.getReward(args[0]);




        if (reward == null) {
            sender.sendMessage(
                    "§c"+LanguageManager.get(
                            "adv_not_found"
                    )
            );
            return true;
        }

        sender.sendMessage(
                "§e"+LanguageManager.get(
                        "reward"
                ) + reward
        );

        return true;
    }
}