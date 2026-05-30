package plugin.scripts.storage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerPointsStorage {

    private static final Map<UUID, Integer> points =
            new HashMap<>();

    public static int getPoints(UUID uuid) {
        return points.getOrDefault(uuid, 0);
    }

    public static void setPoints(UUID uuid, int amount) {
        points.put(uuid, amount);
    }

    public static void addPoints(UUID uuid, int amount) {
        setPoints(uuid, getPoints(uuid) + amount);
    }

    public static Map<UUID, Integer> getAll() {
        return points;
    }
}