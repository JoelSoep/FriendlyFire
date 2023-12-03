package me.joelsoep.friendlyfire;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

import java.util.ArrayList;

@Getter
@Setter
public class Team {
    private final String name;
    private ArrayList<FFPlayer> players = new ArrayList<>();
    private boolean friendlyFire = false;

    public Team(String name) {
        this.name = name;
        Friendlyfire.teams.put(name, this);
    }

    public void addPlayer(FFPlayer player) {
        if (player.getTeam() != null) {
            player.getTeam().removePlayer(player);
        }

        this.players.add(player);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tab player " + player.getPlayer().getName() + " abovename " + this.getName());
        player.setTeam(this);
    }

    public void removePlayer(FFPlayer player) {
        this.players.remove(player);
        player.setTeam(null);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tab player " + player.getPlayer().getName() + " abovename  ");
    }
}
