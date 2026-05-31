package plugin.scripts.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ConfigManager {

    private final Map<String, ConfigOption> options =
            new HashMap<>();

    private final JavaPlugin plugin;
    private final File configFile;

    private double transferTax = 0.10;
    private int heartPrice = 50;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.configFile =
                new File(
                        plugin.getDataFolder(),
                        "config.json"
                );
        options.put(
                "transfer_tax",
                new ConfigOption(
                        "transfer_tax",
                        Double.class,
                        0.0,
                        0.999999999
                )
        );
        options.put(
                "heart_price",
                new ConfigOption(
                        "heart_price",
                        Integer.class,
                        1,
                        1000000
                )
        );
    }

    public void load() {

        try {

            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdirs();
            }

            if (!configFile.exists()) {
                save();
                return;
            }

            try (FileReader reader =
                         new FileReader(configFile)) {

                JsonObject json =
                        new Gson().fromJson(
                                reader,
                                JsonObject.class
                        );

                if (json == null) {
                    return;
                }

                if (json.has("transfer_tax")) {
                    transferTax =
                            json.get("transfer_tax")
                                    .getAsDouble();
                }
                if (json.has("heart_price")) {
                    heartPrice =
                            json.get("heart_price")
                                    .getAsInt();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {

        try {

            JsonObject json =
                    new JsonObject();

            json.addProperty(
                    "transfer_tax",
                    transferTax
            );
            json.addProperty(
                    "heart_price",
                    heartPrice
            );


            try (FileWriter writer =
                         new FileWriter(configFile)) {

                new GsonBuilder()
                        .setPrettyPrinting()
                        .create()
                        .toJson(json, writer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean setValue(
            String name,
            Object value
    ) {

        if (name.equals("transfer_tax")) {

            transferTax = (Double) value;

            save();
            load();

            return true;
        }
        if (name.equals("heart_price")) {

            heartPrice = (Integer) value;

            save();
            load();

            return true;
        }

        return false;
    }

    public double getTransferTax() {
        return transferTax;
    }

    public void setTransferTax(double transferTax) {
        this.transferTax = transferTax;
    }
    public ConfigOption getOption(String name) {
        return options.get(name);
    }
    public int getHeartPrice() {
        return heartPrice;
    }
    public Set<String> getOptionNames() {
        return options.keySet();
    }
}