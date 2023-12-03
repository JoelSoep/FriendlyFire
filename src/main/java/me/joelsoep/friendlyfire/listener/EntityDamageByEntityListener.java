package me.joelsoep.friendlyfire.listener;

import me.joelsoep.friendlyfire.FFPlayer;
import me.joelsoep.friendlyfire.Friendlyfire;
import me.joelsoep.friendlyfire.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntityListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        FFPlayer FFplayer = Friendlyfire.playerList.get(player.getName());
        if (FFplayer == null) {
            Bukkit.getLogger().info("Player " + player.getName() + "is null!");
            return;
        }
        Team playerTeam = FFplayer.getTeam();

        if (event.getDamager() instanceof Arrow arrow) {
            if (!(arrow.getShooter() instanceof Player shooter)) {
                return;
            }
            FFPlayer FFshooter = Friendlyfire.playerList.get(shooter.getName());
            if (FFshooter == null) {
                Bukkit.getLogger().info("Shooter " + shooter.getName() + "is null!");
                return;
            }

            Team shooterTeam = FFshooter.getTeam();

            if (playerTeam == null || shooterTeam == null) {
                return;
            }

            if (playerTeam.getName().equals(shooterTeam.getName()) && !playerTeam.isFriendlyFire()) {
                event.setCancelled(true);
            }
        }

        if (!(event.getDamager() instanceof Player damager)) {
            return;
        }

        FFPlayer FFdamager = Friendlyfire.playerList.get(damager.getName());
        if (FFdamager == null) {
            Bukkit.getLogger().info("Damager " + damager.getName() + "is null!");
            return;
        }

        Team damagerTeam = FFdamager.getTeam();

        if (playerTeam == null || damagerTeam == null) {
            return;
        }

        if (playerTeam.getName().equals(damagerTeam.getName()) && !playerTeam.isFriendlyFire()) {
            event.setCancelled(true);
        }

    }
}
