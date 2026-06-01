package plugin.scripts.core;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import plugin.scripts.ThePartOfLife;
import plugin.scripts.player.PlayerData;
import plugin.scripts.storage.PlayerPointsStorage;
import plugin.scripts.storage.PointLogs;

public class HealingManager
        implements CommandExecutor {

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(
                    "§cКоманда только для игроков."
            );
            return true;
        }

        AttributeInstance health =
                player.getAttribute(
                        Attribute.MAX_HEALTH
                );

        if (health == null) {
            return true;
        }
        if (PlayerData.isDead(
                player.getUniqueId()
        )) {

            player.sendMessage(
                    "§cМёртвые игроки не могут купить сердце"
            );

            return true;
        }

        if (health.getBaseValue() >= 20.0) {

            player.sendMessage(
                    "§cУ вас уже максимальное количество сердец."
            );

            return true;
        }

        int price =
                ThePartOfLife.getInstance()
                        .getConfigManager()
                        .getHeartPrice();

        int points =
                PlayerPointsStorage.getPoints(
                        player.getUniqueId()
                );

        if (points < price) {

            player.sendMessage(
                    "§cНедостаточно очков. Нужно: "
                            + price
            );

            return true;
        }

        PlayerPointsStorage.setPoints(
                player.getUniqueId(),
                points - price
        );

        health.setBaseValue(
                Math.min(
                        20.0,
                        health.getBaseValue() + 2.0
                )
        );

        player.sendMessage(
                "§eВы купили сердце за "
                        + price
                        + " очков."
        );
        PointLogs.log(
                player.getUniqueId(),
                player.getName(),
                "BUY_HEART",
                "price="
                        + price
        );

        return true;
    }
}