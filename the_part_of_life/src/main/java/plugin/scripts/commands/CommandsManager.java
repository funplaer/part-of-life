package plugin.scripts.commands;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import plugin.scripts.core.GhostManager;

public class CommandsManager implements CommandExecutor {

    @Override
    public boolean onCommand( @NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {

        if (!sender.isOp()) {
            sender.sendMessage("§cНедостаточно прав.");
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

        if (args.length != 2) {
            sender.sendMessage("§cИспользование: /addhealth <player> <value>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage("§cИгрок не найден.");
            return true;
        }

        int hearts;

        try {
            hearts = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage("§cValue должно быть числом.");
            return true;
        }

        AttributeInstance attribute =
                target.getAttribute(Attribute.MAX_HEALTH);

        if (attribute == null) {
            sender.sendMessage("§cНе удалось получить здоровье игрока.");
            return true;
        }

        int currentHearts = (int) (attribute.getBaseValue() / 2);

        int newHearts = Math.min(10, currentHearts + hearts);

        attribute.setBaseValue(newHearts * 2);

        sender.sendMessage("§aИгроку добавлено здоровье. Теперь: "
                + newHearts + " сердец.");

        return true;
    }

    private boolean setHealth(CommandSender sender, String[] args) {

        if (args.length != 2) {
            sender.sendMessage("§cИспользование: /sethealth <player> <value>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage("§cИгрок не найден.");
            return true;
        }

        int hearts;

        try {
            hearts = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage("§cValue должно быть числом.");
            return true;
        }

        hearts = Math.max(1, Math.min(10, hearts));

        AttributeInstance attribute =
                target.getAttribute(Attribute.MAX_HEALTH);

        if (attribute == null) {
            sender.sendMessage("§cНе удалось получить здоровье игрока.");
            return true;
        }

        attribute.setBaseValue(hearts * 2);

        if (target.getHealth() > hearts * 2) {
            target.setHealth(hearts * 2);
        }

        sender.sendMessage("§aИгроку установлено "
                + hearts + " сердец.");

        return true;
    }

    private boolean realKill(CommandSender sender, String[] args) {

        if (args.length != 1) {
            sender.sendMessage("§cИспользование: /realkill <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage("§cИгрок не найден.");
            return true;
        }

        GhostManager.makeGhost(target);

        sender.sendMessage("§aИгрок переведен в состояние призрака.");

        return true;
    }

    private boolean respawn(CommandSender sender, String[] args) {

        if (args.length != 1) {
            sender.sendMessage("§cИспользование: /respawn <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage("§cИгрок не найден.");
            return true;
        }

        GhostManager.removeGhost(target);

        AttributeInstance attribute =
                target.getAttribute(Attribute.MAX_HEALTH);

        if (attribute != null) {
            attribute.setBaseValue(20.0);
        }

        target.setHealth(20.0);

        sender.sendMessage("§aИгрок воскрешён.");

        return true;
    }
}