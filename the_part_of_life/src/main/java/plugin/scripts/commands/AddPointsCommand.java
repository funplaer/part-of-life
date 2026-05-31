package plugin.scripts.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
            sender.sendMessage("§cНедостаточно прав.");
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage("§cИспользование: /addpoints <player> <points>");
            return true;
        }

        Player player =
                Bukkit.getPlayerExact(args[0]);

        if (player == null) {
            sender.sendMessage("§cИгрок не найден.");
            return true;
        }

        int points;

        try {
            points = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage("§cНекорректное число.");
            return true;
        }

        PlayerPointsStorage.addPoints(
                player.getUniqueId(),
                points
        );

        sender.sendMessage(
                "§aИгроку " + player.getName()
                        + " добавлено "
                        + points
                        + " очков. (действия админа)"
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