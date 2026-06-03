package plugin.scripts;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class LanguageManager {

    private static final Map<String, String> messages =
            new HashMap<>();

    public static void initialize(
            JavaPlugin plugin,
            String language
    ) {

        try {

            File langFolder =
                    new File(
                            plugin.getDataFolder(),
                            "lang"
                    );

            if (!langFolder.exists()) {

                langFolder.mkdirs();

                copyLanguage(
                        plugin,
                        "ru.json"
                );

                copyLanguage(
                        plugin,
                        "en.json"
                );
            }

            loadLanguage(
                    plugin,
                    language
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void copyLanguage(
            JavaPlugin plugin,
            String fileName
    ) throws IOException {

        InputStream input =
                plugin.getResource(
                        "lang/" + fileName
                );

        if (input == null) {
            return;
        }

        File output =
                new File(
                        plugin.getDataFolder(),
                        "lang/" + fileName
                );

        Files.copy(
                input,
                output.toPath()
        );
    }

    private static void loadLanguage(
            JavaPlugin plugin,
            String language
    ) throws IOException {

        File file =
                new File(
                        plugin.getDataFolder(),
                        "lang/" + language + ".json"
                );

        if (!file.exists()) {

            file =
                    new File(
                            plugin.getDataFolder(),
                            "lang/ru.json"
                    );
        }

        try (FileReader reader =
                     new FileReader(file)) {

            Type type =
                    new TypeToken<
                            Map<String, String>
                            >() {}.getType();

            messages.clear();

            messages.putAll(
                    new Gson().fromJson(
                            reader,
                            type
                    )
            );
        }
    }

    public static String get(
            String key
    ) {

        return messages.getOrDefault(
                key,
                key
        );
    }

    public static String get(
            String key,
            String... replacements
    ) {

        String text =
                get(key);

        for (int i = 0;
             i < replacements.length - 1;
             i += 2) {

            text =
                    text.replace(
                            replacements[i],
                            replacements[i + 1]
                    );
        }

        return text;
    }
}