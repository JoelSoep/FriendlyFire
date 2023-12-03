package me.joelsoep.friendlyfire.listener;

import me.joelsoep.friendlyfire.FFPlayer;
import me.joelsoep.friendlyfire.Friendlyfire;
import me.joelsoep.friendlyfire.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        FFPlayer FFplayer = Friendlyfire.playerList.get(player.getName());
        if (FFplayer == null) {
            return;
        }

        Team team = FFplayer.getTeam();
        if (team == null) {
            return;
        }
        team.removePlayer(FFplayer);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tab player " + player.getName() + " abovename  ");
    }
}
