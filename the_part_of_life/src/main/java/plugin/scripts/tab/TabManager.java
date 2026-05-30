package plugin.scripts.tab;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.scripts.ThePartOfLife;
import plugin.scripts.player.PlayerData;
import plugin.scripts.storage.PlayerPointsStorage;


public class TabManager implements Listener {

    public void startUpdater() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : org.bukkit.Bukkit.getOnlinePlayers()) {
                    update(player);
                }
            }
        }.runTaskTimer(ThePartOfLife.getInstance(), 20L, 20L);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        update(event.getPlayer());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        org.bukkit.Bukkit.getScheduler().runTaskLater(
                ThePartOfLife.getInstance(),
                () -> update(event.getPlayer()),
                10L
        );
    }

    public void update(Player player) {

        if (PlayerData.isDead(player.getUniqueId())) {
            player.playerListName(
                    Component.text(player.getName() + " ")
                            .color(NamedTextColor.DARK_GRAY)
                            .append(Component.text("☠").color(NamedTextColor.GRAY))
            );
            return;
        }

        AttributeInstance attr = player.getAttribute(Attribute.MAX_HEALTH);
        if (attr == null) return;

        int hearts = (int) Math.max(1, Math.round(attr.getBaseValue() / 2.0));



        int points =
                PlayerPointsStorage.getPoints(
                        player.getUniqueId()
                );

        player.playerListName(
                Component.text(player.getName() + " ")
                        .color(NamedTextColor.WHITE)
                        .append(
                                Component.text("❤ " + hearts + " ")
                                        .color(NamedTextColor.RED)
                        )
                        .append(
                                Component.text("★ " + points)
                                        .color(NamedTextColor.GOLD)
                        )
        );
    }
}