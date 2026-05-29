package plugin.scripts.storage;

import java.util.HashMap;
import java.util.Map;

public class AdvancementStorage {

    private static final Map<String, Integer> rewards = new HashMap<>();

    public static void setReward(String advancement, int reward) {
        rewards.put(advancement, reward);
    }

    public static Integer getReward(String advancement) {
        return rewards.get(advancement);
    }

    public static Map<String, Integer> getAll() {
        return rewards;
    }
}