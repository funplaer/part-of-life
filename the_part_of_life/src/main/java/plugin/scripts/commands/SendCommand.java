package plugin.scripts.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import plugin.scripts.ThePartOfLife;
import plugin.scripts.storage.PlayerPointsStorage;
import plugin.scripts.storage.PointLogs;

public class SendCommand implements CommandExecutor {

    //комиссия
    double taxRate =
            ThePartOfLife.getInstance()
                    .getConfigManager()
                    .getTransferTax();

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cКоманда только для игроков.");
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage("§cИспользование: /send <player> <points>");
            return true;
        }

        Player target =
                Bukkit.getPlayerExact(args[0]);

        if (target == null) {
            player.sendMessage("§cИгрок не найден.");
            return true;
        }

        if (target.equals(player)) {
            player.sendMessage("§cНельзя отправить очки самому себе.");
            return true;
        }

        int amount;

        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage("§cНекорректное число.");
            return true;
        }

        if (amount <= 0) {
            player.sendMessage("§cКоличество должно быть больше 0.");
            return true;
        }

        int senderPoints =
                PlayerPointsStorage.getPoints(
                        player.getUniqueId()
                );

        if (senderPoints < amount) {
            player.sendMessage("§cНедостаточно очков.");
            return true;
        }

        int received =
                (int) Math.floor(
                        amount * (1.0 - taxRate)
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
                "§eВы отправили " + received
                        + " очков игроку "
                        + target.getName()
                        + ". Комиссия: "
                        + tax
        );
        PointLogs.log(
                player.getUniqueId(),
                player.getName(),
                "TRANSFER_SENT",
                "to="
                        + target.getName()
                        + ", amount="
                        + amount
                        + ", received="
                        + received
        );
        PointLogs.log(
                target.getUniqueId(),
                target.getName(),
                "TRANSFER_RECEIVED",
                "from="
                        + player.getName()
                        + ", amount="
                        + received
        );

        target.sendMessage("§e "+
                player.getName()
                        + " отправил вам "
                        + received
                        + " очков."
        );

        return true;
    }
}