package me.joelsoep.friendlyfire;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter
@Setter
public class FFPlayer {
    private final Player player;
    private Team team;

    public FFPlayer(Player player) {
        this.player = player;
        Friendlyfire.playerList.put(player.getName(), this);
    }
}
