package plugin.scripts.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import plugin.scripts.storage.PlayerPointsStorage;

public class SendCommand implements CommandExecutor {

    //комиссия
    private static final double TAX = 0.10;

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Команда только для игроков.");
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage("/send <player> <points>");
            return true;
        }

        Player target =
                Bukkit.getPlayerExact(args[0]);

        if (target == null) {
            player.sendMessage("Игрок не найден.");
            return true;
        }

        if (target.equals(player)) {
            player.sendMessage("Нельзя отправить очки самому себе.");
            return true;
        }

        int amount;

        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage("Некорректное число.");
            return true;
        }

        if (amount <= 0) {
            player.sendMessage("Количество должно быть больше 0.");
            return true;
        }

        int senderPoints =
                PlayerPointsStorage.getPoints(
                        player.getUniqueId()
                );

        if (senderPoints < amount) {
            player.sendMessage("Недостаточно очков.");
            return true;
        }

        int received =
                (int) Math.floor(
                        amount * (1.0 - TAX)
                );

        int tax =
                amount - received;

        PlayerPointsStorage.setPoints(
                player.getUniqueId(),
                senderPoints - amount
        );

        PlayerPointsStorage.addPoints(
                target.getUniqueId(),
                received
        );

        player.sendMessage(
                "Вы отправили " + received
                        + " очков игроку "
                        + target.getName()
                        + ". Комиссия: "
                        + tax
        );

        target.sendMessage(
                player.getName()
                        + " отправил вам "
                        + received
                        + " очков."
        );

        return true;
    }
}