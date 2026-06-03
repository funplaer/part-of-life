package plugin.scripts.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import plugin.scripts.LanguageManager;
import plugin.scripts.ThePartOfLife;
import plugin.scripts.player.PlayerData;
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
            sender.sendMessage("§c"+ LanguageManager.get(
                    "only_for_players"
            ));
            return true;
        }


        if (args.length != 2) {
            sender.sendMessage("§c"+ LanguageManager.get(
                    "usage_send"
            ));
            return true;
        }
        if (PlayerData.isDead(
                player.getUniqueId()
        )) {

            player.sendMessage(
                    "§c" + LanguageManager.get(
                            "send_by_dead"
                    )
            );

            return true;
        }

        Player target =
                Bukkit.getPlayerExact(args[0]);

        if (target == null) {
            player.sendMessage("§c"+ LanguageManager.get(
                    "player_not_found"
            ));
            return true;
        }
        if (PlayerData.isDead(
                target.getUniqueId()
        )) {

            player.sendMessage(
                    "§c" + LanguageManager.get(
                            "send_to_dead"
                    )
            );

            return true;
        }


        if (target.equals(player)) {
            player.sendMessage("§c" + LanguageManager.get(
                    "send_self"
            ));
            return true;
        }

        int amount;

        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage("§c" + LanguageManager.get(
                    "wrong_number"
            ));
            return true;
        }

        if (amount <= 0) {
            player.sendMessage("§c" + LanguageManager.get(
                    "send_more_zero"
            ));
            return true;
        }

        int senderPoints =
                PlayerPointsStorage.getPoints(
                        player.getUniqueId()
                );

        if (senderPoints < amount) {
            player.sendMessage("§c" + LanguageManager.get(
                    "no_points"
            ));
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
                "§e" + LanguageManager.get(
                        "you_send"
                ) + received
                        + LanguageManager.get(
                        "points_to"
                )
                        + target.getName()
                        + LanguageManager.get(
                        "tax"
                )
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

        target.sendMessage("§e"+
                player.getName()
                        + LanguageManager.get(
                        "send_you"
                )
                        + received
                        + LanguageManager.get(
                        "points"
                )
        );

        return true;
    }
}