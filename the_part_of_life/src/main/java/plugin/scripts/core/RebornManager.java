package plugin.scripts.core;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import plugin.scripts.ThePartOfLife;
import plugin.scripts.core.GhostManager;
import plugin.scripts.player.PlayerData;
import plugin.scripts.storage.PlayerPointsStorage;
import plugin.scripts.storage.PointLogs;

public class RebornManager
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


        if (args.length != 1) {

            player.sendMessage(
                    "§cИспользование: /reborn <player>"
            );

            return true;
        }
        if (PlayerData.isDead(
                player.getUniqueId()
        )) {

            player.sendMessage(
                    "§cМёртвые игроки не могут проводить возрождение."
            );

            return true;
        }


        Player target =
                Bukkit.getPlayer(args[0]);

        if (!PlayerData.isDead(
                target.getUniqueId()
        )) {

            player.sendMessage(
                    "§cЭтот игрок не мёртв."
            );

            return true;
        }

        if (target == null) {

            player.sendMessage(
                    "§cИгрок не найден."
            );

            return true;
        }
        if (target.equals(player)) {

            player.sendMessage(
                    "§cНельзя возродить самого себя."
            );

            return true;
        }

        int price =
                ThePartOfLife.getInstance()
                        .getConfigManager()
                        .getRebornPrice();

        int points =
                PlayerPointsStorage.getPoints(
                        player.getUniqueId()
                );

        if (points < price) {

            player.sendMessage(
                    "§cНедостаточно очков."
            );

            return true;
        }

        if (!player.getInventory()
                .contains(Material.DRAGON_EGG)) {

            player.sendMessage(
                    "§cДля возрождения требуется яйцо дракона в инвентаре."
            );

            return true;
        }

        PlayerPointsStorage.setPoints(
                player.getUniqueId(),
                points - price
        );



        GhostManager.removeGhost(target);
        for (Player online : Bukkit.getOnlinePlayers()) {

            online.playSound(
                    online.getLocation(),
                    Sound.BLOCK_END_PORTAL_SPAWN,
                    1.0f,
                    1.0f
            );

            online.playSound(
                    online.getLocation(),
                    Sound.UI_TOAST_CHALLENGE_COMPLETE,
                    1.0f,
                    0.5f
            );
            target.getWorld().strikeLightningEffect(
                    target.getLocation()
            );
        }

        AttributeInstance attribute =
                target.getAttribute(
                        Attribute.MAX_HEALTH
                );

        int health =
                ThePartOfLife.getInstance()
                        .getConfigManager()
                        .getRebornHealth();

        if (attribute != null) {

            attribute.setBaseValue(
                    health
            );
        }
        health = Math.max(
                2,
                Math.min(
                        20,
                        health
                )
        );

        if (health % 2 != 0) {
            health = 10;
        }

        target.setHealth(
                Math.min(
                        health,
                        attribute != null
                                ? attribute.getBaseValue()
                                : health
                )
        );

        Bukkit.broadcastMessage("§6 " +
                player.getName()
                        + " возродил игрока "
                        + target.getName()
        );
        PointLogs.log(
                player.getUniqueId(),
                player.getName(),
                "REBORN",
                "target="
                        + target.getName()
                        + ", price="
                        + price
        );

        return true;
    }
}