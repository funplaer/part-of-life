package plugin.scripts.listeners;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;


import plugin.scripts.storage.PlayerPointsStorage;
import plugin.scripts.storage.AdvancementStorage;

public class AdvancementListener implements Listener {

    @EventHandler
    public void onAdvancement(PlayerAdvancementDoneEvent event) {

        String advancementId = null;



        Player player = event.getPlayer();

        advancementId = event.getAdvancement()
                .getKey()
                .getKey()
                .substring(event.getAdvancement().getKey().getKey().lastIndexOf('/') + 1)  + ".json";

        int reward =
                AdvancementStorage.getReward(advancementId) != null
                        ? AdvancementStorage.getReward(advancementId)
                        : 0;



        if (reward != 0) {

            PlayerPointsStorage.addPoints(
                    player.getUniqueId(),
                    reward
            );
        }




    }
}