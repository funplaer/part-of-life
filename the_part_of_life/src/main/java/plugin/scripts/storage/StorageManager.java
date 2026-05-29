package plugin.scripts.storage;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.FileReader;

public class StorageManager {

    private final JavaPlugin plugin;

    public StorageManager (JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadRewards() {

        Gson gson = new Gson();

        for (World world : Bukkit.getWorlds()) {

            File datapacksFolder =
                    new File(world.getWorldFolder(), "datapacks");

            scanFolder(datapacksFolder, gson);
        }


    }


    private void scanFolder(File folder, Gson gson) {

        if (!folder.exists()) return;

        File[] files = folder.listFiles();

        if (files == null) return;

        for (File file : files) {

            if (file.isDirectory()) {
                scanFolder(file, gson);
                continue;
            }

            if (!file.getName().endsWith(".json")) {
                continue;
            }

            try (FileReader reader = new FileReader(file)) {

                JsonObject json =
                        gson.fromJson(reader, JsonObject.class);

                if (!json.has("point_reward")) {
                    continue;
                }

                int reward =
                        json.get("point_reward").getAsInt();

                String id =
                        buildAdvancementId(file);

                AdvancementStorage.setReward(id, reward);

            } catch (Exception e) {
                plugin.getLogger().warning(
                        "Failed to read " + file.getName()
                );
            }
        }
    }

    private String buildAdvancementId(File file) {

        String path =
                file.getPath().replace("\\", "/");

        int dataIndex = path.indexOf("/data/");

        if (dataIndex == -1) {
            return file.getName();
        }

        String relative =
                path.substring(dataIndex + 6);

        String[] split = relative.split("/");

        if (split.length < 3) {
            return file.getName();
        }

        String namespace = split[0];

        int advancementsIndex =
                relative.indexOf("/advancements/");

        if (advancementsIndex == -1) {
            return file.getName();
        }

        String advancementPath =
                relative.substring(
                        advancementsIndex +
                                "/advancements/".length()
                );

        advancementPath =
                advancementPath.replace(".json", "");

        return namespace + ":" + advancementPath;
    }
}