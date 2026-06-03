package plugin.scripts.core;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import plugin.scripts.LanguageManager;
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
                    "§c"+ LanguageManager.get(
                            "only_for_players"
                    )
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
                    "§c"+ LanguageManager.get(
                            "dead_cant_buy_heart"
                    )
            );

            return true;
        }

        if (health.getBaseValue() >= 20.0) {

            player.sendMessage(
                    "§c"+LanguageManager.get(
                            "already_full_hearts"
                    )
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
                    "§c" + LanguageManager.get(
                            "no_points_need"
                    )
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
                "§e"+ LanguageManager.get(
                        "you_buy_heart"
                )
                        + price
                        + LanguageManager.get(
                        "buyheart_points"
                )
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