package plugin.scripts.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import plugin.scripts.LanguageManager;
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
                    "§c"+ LanguageManager.get(
                            "player_not_admin"
                    )
            );

            return true;
        }

        if (args.length != 2) {

            sender.sendMessage(
                    "§c"+LanguageManager.get(
                            "usage_ptconfig"
                    )
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
                    "§c"+LanguageManager.get(
                            "unexpected_var"
                    )
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
                            "§c"+ LanguageManager.get(
                                    "value_must_be"
                            )
                                    + option.getMin()
                                    + LanguageManager.get(
                                    "to"
                            )
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
                            "§c"+LanguageManager.get(
                                    "value_must_be"
                            )
                                    + option.getMin()
                                    + LanguageManager.get(
                                    "to"
                            )
                                    + option.getMax()
                    );
                    if (optionName.equals("reborn_health")
                            && value % 2 != 0) {

                        sender.sendMessage(
                                "§c"+LanguageManager.get(
                                        "hp_must_be_even"
                                )
                        );

                        return true;
                    }

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
                            "§c"+LanguageManager.get(
                                    "only_boolean"
                            )
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
                    "§c"+LanguageManager.get(
                            "wrong_value_type"
                    )
            );

            return true;
        }


        config.setValue(
                optionName,
                parsedValue
        );

        sender.sendMessage(
                "§a"+ LanguageManager.get(
                        "parameter"
                )
                        + optionName
                        + LanguageManager.get(
                        "changed_to"
                )
                        + parsedValue
        );


        ConfigManager configManager = new ConfigManager(ThePartOfLife.getInstance());
        configManager.load();

        return true;
    }
}