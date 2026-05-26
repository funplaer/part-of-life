package plugin.scripts.listeners;

import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import plugin.scripts.ThePartOfLife;
import plugin.scripts.player.PlayerData;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DeathListener implements Listener {

    private final Set<UUID> toSpectator = new HashSet<>();
    private final Set<UUID> toReduceHealth = new HashSet<>();

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        AttributeInstance attr = player.getAttribute(Attribute.MAX_HEALTH);
        if (attr == null) return;

        double maxHealth = attr.getBaseValue();

        if (maxHealth <= 2.0) {
            toSpectator.add(player.getUniqueId());
            PlayerData.markDead(player.getUniqueId());
        } else {
            toReduceHealth.add(player.getUniqueId());
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (toSpectator.remove(uuid)) {
            player.getServer().getScheduler().runTask(
                    ThePartOfLife.getInstance(),
                    () -> player.setGameMode(GameMode.SPECTATOR)
            );
            return;
        }

        if (toReduceHealth.remove(uuid)) {
            player.getServer().getScheduler().runTask(
                    ThePartOfLife.getInstance(),
                    () -> {
                        AttributeInstance attr = player.getAttribute(Attribute.MAX_HEALTH);
                        if (attr == null) return;

                        double current = attr.getBaseValue();
                        double newValue = Math.max(2.0, current - 2.0);
                        PlayerData.markAlive(player.getUniqueId());

                        attr.setBaseValue(newValue);

                        if (player.getHealth() > newValue) {
                            player.setHealth(newValue);
                        }
                    }
            );
        }
    }
}