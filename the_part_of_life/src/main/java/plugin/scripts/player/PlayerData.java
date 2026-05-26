package plugin.scripts.player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerData {

    private static final Set<UUID> deadPlayers = new HashSet<>();

    public static void markDead(UUID uuid) {
        deadPlayers.add(uuid);
    }

    public static void markAlive(UUID uuid) {
        deadPlayers.remove(uuid);
    }

    public static boolean isDead(UUID uuid) {
        return deadPlayers.contains(uuid);
    }
}