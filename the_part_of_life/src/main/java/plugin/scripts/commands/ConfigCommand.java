package plugin.scripts.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import plugin.scripts.ThePartOfLife;
import plugin.scripts.config.ConfigManager;
import plugin.scripts.config.ConfigOption;

public class ConfigCommand
        implements CommandExecutor {
    


    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (!sender.hasPermission(
                "partoflife.admin")) {

            sender.sendMessage(
                    "§cНет прав."
            );

            return true;
        }

        if (args.length != 2) {

            sender.sendMessage(
                    "§cИспользование: /ptconfig <option> <value>"
            );

            return true;
        }

        ConfigManager config =
                ThePartOfLife.getInstance()
                        .getConfigManager();

        String optionName =
                args[0].toLowerCase();

        ConfigOption option =
                config.getOption(optionName);

        if (option == null) {

            sender.sendMessage(
                    "§cПеременная не существует."
            );

            return true;
        }

        Object parsedValue;

        try {

            if (option.getType()
                    == Double.class) {

                double value =
                        Double.parseDouble(
                                args[1]
                        );

                if (value < option.getMin()
                        || value > option.getMax()) {

                    sender.sendMessage(
                            "§cЗначение должно быть от "
                                    + option.getMin()
                                    + " до "
                                    + option.getMax()
                    );

                    return true;
                }

                parsedValue = value;

            } else if (
                    option.getType()
                            == Integer.class
            ) {

                int value =
                        Integer.parseInt(
                                args[1]
                        );

                if (value < option.getMin()
                        || value > option.getMax()) {

                    sender.sendMessage(
                            "§cЗначение должно быть от "
                                    + option.getMin()
                                    + " до "
                                    + option.getMax()
                    );

                    return true;
                }

                parsedValue = value;

            } else if (
                    option.getType()
                            == Boolean.class
            ) {

                if (!args[1]
                        .equalsIgnoreCase("true")
                        && !args[1]
                        .equalsIgnoreCase("false")) {

                    sender.sendMessage(
                            "§cДопустимо только true или false"
                    );

                    return true;
                }

                parsedValue =
                        Boolean.parseBoolean(
                                args[1]
                        );

            } else {

                parsedValue = args[1];
            }

        } catch (Exception e) {

            sender.sendMessage(
                    "§cНеверный тип значения."
            );

            return true;
        }

        config.setValue(
                optionName,
                parsedValue
        );

        sender.sendMessage(
                "§aПараметр "
                        + optionName
                        + " изменён на "
                        + parsedValue
        );


        ConfigManager configManager = new ConfigManager(ThePartOfLife.getInstance());
        configManager.load();

        return true;
    }
}