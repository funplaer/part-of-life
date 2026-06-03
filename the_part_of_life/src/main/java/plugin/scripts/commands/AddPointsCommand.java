package plugin.scripts.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import plugin.scripts.LanguageManager;
import plugin.scripts.storage.PlayerPointsStorage;
import plugin.scripts.storage.PointLogs;

public class AddPointsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (!sender.hasPermission("partoflife.admin")) {
            sender.sendMessage("§c"+LanguageManager.get(
                    "player_not_admin"
            ));
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage("§c"+ LanguageManager.get(
                    "usage_addpoints"
            ));
            return true;
        }

        Player player =
                Bukkit.getPlayerExact(args[0]);

        if (player == null) {
            sender.sendMessage("§c"+LanguageManager.get(
                    "player_not_found"
            ));
            return true;
        }

        int points;

        try {
            points = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage("§c"+ LanguageManager.get(
                    "wrong_number"
            ));
            return true;
        }

        PlayerPointsStorage.addPoints(
                player.getUniqueId(),
                points
        );

        sender.sendMessage(
                "§a"+ LanguageManager.get(
                        "1_1"
                ) + player.getName()
                        + LanguageManager.get(
                        "1_2"
                )
                        + points
                        + LanguageManager.get(
                        "1_3"
                )
        );
        PointLogs.log(
                player.getUniqueId(),
                player.getName(),
                "ADMIN-ADDPOINTS",
                "Admin - ["
                        + sender.getName() + "]"
                        + " points - "
                        + points
        );

        return true;
    }
}