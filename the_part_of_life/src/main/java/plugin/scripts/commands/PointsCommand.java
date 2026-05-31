package plugin.scripts.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
                    "§cИспользование: /showreward <advancement>"
            );
            return true;
        }

        Integer reward =
                AdvancementStorage.getReward(args[0]);




        if (reward == null) {
            sender.sendMessage(
                    "§cДостижение не найдено."
            );
            return true;
        }

        sender.sendMessage(
                "§eНаграда: " + reward
        );

        return true;
    }
}