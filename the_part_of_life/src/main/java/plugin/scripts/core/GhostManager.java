package plugin.scripts.core;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import plugin.scripts.player.PlayerData;

import java.util.UUID;

public class GhostManager implements Listener {

    private static final String GHOST_TEAM = "ghosts";



    public static void makeGhost(Player player) {

        UUID uuid = player.getUniqueId();
        PlayerData.markDead(uuid);

        player.setGameMode(GameMode.ADVENTURE);

        // полёт как в креативе
        player.setAllowFlight(true);
        player.setFlying(true);

        // голод всегда полный
        player.setFoodLevel(20);
        player.setSaturation(20f);

        // нельзя получать урон
        player.setInvulnerable(true);

        setupTeam(player);

        updateName(player);
    }

    private static void setupTeam(Player player) {
        Scoreboard board = player.getServer().getScoreboardManager().getMainScoreboard();

        Team team = board.getTeam(GHOST_TEAM);
        if (team == null) {
            team = board.registerNewTeam(GHOST_TEAM);

            team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        }

        team.addEntry(player.getName());
    }

    public static void updateName(Player player) {
        player.playerListName(
                Component.text(player.getName() + " ")
                        .color(NamedTextColor.GRAY)
                        .append(Component.text("[МЁРТВ]").color(NamedTextColor.DARK_GRAY))
        );
    }

    //без взаимодействий с блоками

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (PlayerData.isDead(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (PlayerData.isDead(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (!PlayerData.isDead(e.getPlayer().getUniqueId())) return;

        switch (e.getClickedBlock() == null ? "" : e.getClickedBlock().getType().toString()) {

            case "OAK_DOOR":
            case "BIRCH_DOOR":
            case "JUNGLE_DOOR":
            case "ACACIA_DOOR":
            case "DARK_OAK_DOOR":
            case "CHERRY_DOOR":
            case "MANGROVE_DOOR":
            case "SPRUCE_DOOR":
            case "BAMBOO_DOOR":
            case "CRIMSON_DOOR":
            case "WARPED_DOOR":
            case "COPPER_DOOR":
            case "EXPOSED_COPPER_DOOR":
            case "PALE_OAK_DOOR":
            case "WEATHERED_COPPER_DOOR":
            case "OXIDIZED_COPPER_DOOR":
            case "WAXED_COPPER_DOOR":
            case "WAXED_EXPOSED_COPPER_DOOR":
            case "WAXED_WEATHERED_COPPER_DOOR":
            case "WAXED_OXIDIZED_COPPER_DOOR":

            case "OAK_TRAPDOOR":
            case "BIRCH_TRAPDOOR":
            case "JUNGLE_TRAPDOOR":
            case "ACACIA_TRAPDOOR":
            case "DARK_OAK_TRAPDOOR":
            case "CHERRY_TRAPDOOR":
            case "MANGROVE_TRAPDOOR":
            case "SPRUCE_TRAPDOOR":
            case "BAMBOO_TRAPDOOR":
            case "CRIMSON_TRAPDOOR":
            case "WARPED_TRAPDOOR":
            case "COPPER_TRAPDOOR":
            case "EXPOSED_COPPER_TRAPDOOR":
            case "PALE_OAK_TRAPDOOR":
            case "WEATHERED_COPPER_TRAPDOOR":
            case "OXIDIZED_COPPER_TRAPDOOR":
            case "WAXED_COPPER_TRAPDOOR":
            case "WAXED_EXPOSED_COPPER_TRAPDOOR":
            case "WAXED_WEATHERED_COPPER_TRAPDOOR":
            case "WAXED_OXIDIZED_COPPER_TRAPDOOR":
                return;
        }

        e.setCancelled(true);
    }

    //предметы

    @EventHandler
    public void onPickup(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player p &&
                PlayerData.isDead(p.getUniqueId())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (PlayerData.isDead(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventory(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player p &&
                PlayerData.isDead(p.getUniqueId())) {
            e.setCancelled(true);
        }
    }

    //дамаг и мобы

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {

        if (e.getEntity() instanceof Player p &&
                PlayerData.isDead(p.getUniqueId())) {
            e.setCancelled(true);
        }

        if (e.getDamager() instanceof Player p2 &&
                PlayerData.isDead(p2.getUniqueId())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onTarget(EntityTargetLivingEntityEvent e) {

        if (e.getTarget() instanceof Player p &&
                PlayerData.isDead(p.getUniqueId())) {
            e.setCancelled(true);
        }
    }

    //еда

    @EventHandler
    public void onFood(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player p &&
                PlayerData.isDead(p.getUniqueId())) {
            e.setFoodLevel(20);
            e.setCancelled(true);
        }
    }

    public static void removeGhost(Player player) {

        UUID uuid = player.getUniqueId();
        PlayerData.markAlive(uuid);


        player.setGameMode(GameMode.SURVIVAL);


        player.setFlying(false);
        player.setAllowFlight(false);


        player.setInvulnerable(false);


        player.setFoodLevel(20);
        player.setSaturation(20f);


        Scoreboard board = player.getServer().getScoreboardManager().getMainScoreboard();
        Team team = board.getTeam(GHOST_TEAM);
        if (team != null) {
            team.removeEntry(player.getName());
        }


    }

}