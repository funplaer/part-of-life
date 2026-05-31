package plugin.scripts.storage;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class PointLogs {

    private static File logFile;

    public static void initialize(
            JavaPlugin plugin
    ) {

        try {

            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdirs();
            }

            logFile = new File(
                    plugin.getDataFolder(),
                    "pointlogs.txt"
            );

            if (!logFile.exists()) {
                logFile.createNewFile();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void log(
            UUID uuid,
            String nickname,
            String action,
            String details
    ) {

        try {

            String time =
                    LocalDateTime.now()
                            .format(
                                    DateTimeFormatter.ofPattern(
                                            "yyyy-MM-dd HH:mm:ss"
                                    )
                            );

            String line =
                    "[" + time + "] "
                            + "[" + uuid + "] "
                            + "[" + nickname + "] "
                            + action
                            + " | "
                            + details
                            + System.lineSeparator();

            try (FileWriter writer =
                         new FileWriter(
                                 logFile,
                                 true
                         )) {

                writer.write(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}