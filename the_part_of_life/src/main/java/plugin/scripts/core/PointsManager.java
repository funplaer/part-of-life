package plugin.scripts.core;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import plugin.scripts.storage.AdvancementStorage;

public class PointsManager implements CommandExecutor {

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (args.length != 1) {
            sender.sendMessage(
                    "/showreward <advancement>"
            );
            return true;
        }

        Integer reward =
                AdvancementStorage.getReward(args[0]);

        if (reward == null) {
            sender.sendMessage(
                    "Достижение не найдено."
            );
            return true;
        }

        sender.sendMessage(
                "Награда: " + reward
        );

        return true;
    }
}