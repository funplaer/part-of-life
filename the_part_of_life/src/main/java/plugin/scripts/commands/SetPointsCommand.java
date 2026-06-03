package plugin.scripts.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import plugin.scripts.LanguageManager;
import plugin.scripts.storage.PlayerPointsStorage;
import plugin.scripts.storage.PointLogs;

public class SetPointsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (!sender.hasPermission("partoflife.admin")) {
            sender.sendMessage("§c"+ LanguageManager.get(
                    "player_not_admin"
            ));
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage("§c" + LanguageManager.get(
                    "usage_setpoints"
            ));
            return true;
        }

        Player player =
                Bukkit.getPlayerExact(args[0]);

        if (player == null) {
            sender.sendMessage("§c"+ LanguageManager.get(
                    "player_not_found"
            ));
            return true;
        }

        int points;

        try {
            points = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage("§c" + LanguageManager.get(
                    "wrong_number"
            ));
            return true;
        }

        PlayerPointsStorage.setPoints(
                player.getUniqueId(),
                points
        );

        sender.sendMessage(
                "§a"+ LanguageManager.get(
                        "for_player"
                ) + player.getName()
                        + LanguageManager.get(
                        "setted"
                )
                        + points
                        + LanguageManager.get(
                        "setted_points"
                )
        );
        PointLogs.log(
                player.getUniqueId(),
                player.getName(),
                "ADMIN-SETPOINTS",
                "Admin - ["
                        + sender.getName() + "]"
                        + " points - "
                        + points
        );

        return true;
    }
}