package plugin.scripts.commands;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import plugin.scripts.LanguageManager;
import plugin.scripts.core.GhostManager;

public class CommandsManager implements CommandExecutor {

    @Override
    public boolean onCommand( @NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {

        if (!sender.isOp()) {
            sender.sendMessage("§c"+ LanguageManager.get(
                    "player_not_admin"
            ));
            return true;
        }

        switch (command.getName().toLowerCase()) {

            case "addhealth":
                return addHealth(sender, args);

            case "sethealth":
                return setHealth(sender, args);

            case "realkill":
                return realKill(sender, args);

            case "respawn":
                return respawn(sender, args);
        }

        return false;
    }

    private boolean addHealth(CommandSender sender, String[] args) {
        Player player =
                Bukkit.getPlayerExact(args[0]);
        if (player == null) {
            sender.sendMessage("§c"+ LanguageManager.get(
                    "player_not_found"
            ));
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage("§c"+ LanguageManager.get(
                    "usage_addhealth"
            ));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage("§c"+LanguageManager.get(
                    "player_not_found"
            ));
            return true;
        }

        int hearts;

        try {
            hearts = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage("§c"+LanguageManager.get(
                    "wrong_number"
            ));
            return true;
        }

        AttributeInstance attribute =
                target.getAttribute(Attribute.MAX_HEALTH);

        if (attribute == null) {
            sender.sendMessage("§c"+LanguageManager.get(
                    "none_health"
            ));
            return true;
        }

        int currentHearts = (int) (attribute.getBaseValue() / 2);

        int newHearts = Math.min(10, currentHearts + hearts);

        attribute.setBaseValue(newHearts * 2);

        sender.sendMessage("§a"+ LanguageManager.get(
                "2_1"
        ) + player.getName()+ LanguageManager.get(
                "2_2"
        )
                + newHearts + LanguageManager.get(
                "2_3"
        ));

        return true;
    }

    private boolean setHealth(CommandSender sender, String[] args) {
        Player player =
                Bukkit.getPlayerExact(args[0]);
        if (player == null) {
            sender.sendMessage("§c"+ LanguageManager.get(
                    "player_not_found"
            ));
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage("§c"+LanguageManager.get(
                    "usage_sethealth"
            ));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage("§c"+LanguageManager.get(
                    "player_not_found"
            ));
            return true;
        }

        int hearts;

        try {
            hearts = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage("§c"+LanguageManager.get(
                    "wrong_number"
            ));
            return true;
        }

        hearts = Math.max(1, Math.min(10, hearts));

        AttributeInstance attribute =
                target.getAttribute(Attribute.MAX_HEALTH);

        if (attribute == null) {
            sender.sendMessage("§c"+LanguageManager.get(
                    "none_health"
            ));
            return true;
        }

        attribute.setBaseValue(hearts * 2);

        if (target.getHealth() > hearts * 2) {
            target.setHealth(hearts * 2);
        }

        sender.sendMessage("§a"+ LanguageManager.get(
                "3_1"
        )+ player.getName()+LanguageManager.get(
                "3_2"
        )
                + hearts + LanguageManager.get(
                "3_3"
        ));

        return true;
    }

    private boolean realKill(CommandSender sender, String[] args) {
        Player player =
                Bukkit.getPlayerExact(args[0]);
        if (player == null) {
            sender.sendMessage("§c"+LanguageManager.get(
                    "player_not_found"
            ));
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage("§c"+ LanguageManager.get(
                    "usage_realkill"
            ));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage("§c"+LanguageManager.get(
                    "player_not_found"
            ));
            return true;
        }

        GhostManager.makeGhost(target);


        sender.sendMessage("§a"+LanguageManager.get(
                "4_1"
        )+ player.getName() + LanguageManager.get(
                "4_2"
        ));

        return true;
    }

    private boolean respawn(CommandSender sender, String[] args) {
        Player player =
                Bukkit.getPlayerExact(args[0]);
        if (player == null) {
            sender.sendMessage("§c"+LanguageManager.get(
                    "player_not_found"
            ));
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage("§c"+LanguageManager.get(
                    "usage_respawn"
            ));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage("§c"+LanguageManager.get(
                    "player_not_found"
            ));
            return true;
        }

        GhostManager.removeGhost(target);

        AttributeInstance attribute =
                target.getAttribute(Attribute.MAX_HEALTH);

        if (attribute != null) {
            attribute.setBaseValue(20.0);
        }

        target.setHealth(20.0);

        sender.sendMessage("§a"+LanguageManager.get(
                "5_1"
        ) + player.getName() + LanguageManager.get(
                "5_2"
        ));

        return true;
    }
}