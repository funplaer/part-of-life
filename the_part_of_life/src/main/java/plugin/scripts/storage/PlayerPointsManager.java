package plugin.scripts.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerPointsManager {

    private final JavaPlugin plugin;
    private final File file;

    public PlayerPointsManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(
                plugin.getDataFolder(),
                "playerpoints.json"
        );
    }

    public void load() {

        if (!file.exists()) {
            return;
        }

        try (FileReader reader = new FileReader(file)) {

            Type type =
                    new TypeToken<HashMap<String, Integer>>(){}.getType();

            Map<String, Integer> data =
                    new Gson().fromJson(reader, type);

            if (data == null) {
                return;
            }

            for (var entry : data.entrySet()) {

                PlayerPointsStorage.setPoints(
                        UUID.fromString(entry.getKey()),
                        entry.getValue()
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {

        try {

            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdirs();
            }

            Map<String, Integer> data =
                    new HashMap<>();

            for (var entry :
                    PlayerPointsStorage.getAll().entrySet()) {

                data.put(
                        entry.getKey().toString(),
                        entry.getValue()
                );
            }

            try (FileWriter writer =
                         new FileWriter(file)) {

                new GsonBuilder()
                        .setPrettyPrinting()
                        .create()
                        .toJson(data, writer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}