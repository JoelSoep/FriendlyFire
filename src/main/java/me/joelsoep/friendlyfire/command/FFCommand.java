package me.joelsoep.friendlyfire.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import me.joelsoep.friendlyfire.FFPlayer;
import me.joelsoep.friendlyfire.Friendlyfire;
import me.joelsoep.friendlyfire.Team;
import me.joelsoep.friendlyfire.util.MM;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@CommandPermission("ff.admin")
@CommandAlias("friendlyfire|ff|ct")
public class FFCommand extends BaseCommand {

    @Default
    @CatchUnknown
    @Subcommand("help")
    public void onHelp(Player player) {
        player.sendMessage(MM.deserialize("""
                <gray>FriendlyFire by JoelSoep
                <gray>------------------------
                <gray>/ct create [TEAM_NAME] (Ensure Color Codes work)
                <gray>/ct delete [TEAM_NAME]
                <gray>/ct assign [PLAYER] [TEAM_NAME]
                <gray>/ct remove [PLAYER] (Removes player from a team / makes the player teamless)
                <gray>/ct radius [TEAM_NAME] (Assigns players within a 15 block radius to the certain team implied)
                <gray>/ct list [TEAM_NAME] (List Players on a team)
                <gray>/ct listteams (List all created teams)
                <gray>/ct friendlyfire [TEAM_NAME] [BOOLEAN] (Manage the friendlyfire status of the team.)
                <gray>/ct clear [TEAM_NAME] (Remove all players from a team)
                <gray>/ct nametags (Disable or enable nametags globally)
                <gray>/ct assignall (Assign all players who aren't in a team to a team.)
                """));
    }

    @Subcommand("create")
    @Description("/ff create <name> - creates a new team.")
    public void create(Player player, @Single String name) {
        if (Friendlyfire.teams.get(name) != null) {
            player.sendMessage(MM.deserialize("<red>This team already exists!"));
            return;
        }
        new Team(name);
        player.sendMessage(MM.deserialize("<gray>Successfully created team <yellow>" + name));
    }

    @Subcommand("delete")
    public void delete(Player player, @Single String name) {
        if (Friendlyfire.teams.get(name) == null) {
            player.sendMessage(MM.deserialize("<red>Team <yellow>" + name + " <red>does not exist!"));
            return;
        }
        Team team = Friendlyfire.teams.get(name);
        List<FFPlayer> playersToRemove = new ArrayList<>(team.getPlayers());
        playersToRemove.forEach(team::removePlayer);
        Friendlyfire.teams.remove(team.getName());
        player.sendMessage(MM.deserialize("<gray>Deleted team <yellow>" + name + "<gray>."));
    }

    @Subcommand("assign")
    @CommandCompletion("@players")
    public void assign(Player player, OnlinePlayer target, @Single String team) {
        if (Friendlyfire.playerList.get(target.getPlayer().getName()) == null) {
            player.sendMessage(MM.deserialize("<red>Couldn't find player <yellow>" + target.getPlayer().getName() + "<red>!"));
            return;
        }

        if (Friendlyfire.teams.get(team) == null) {
            player.sendMessage(MM.deserialize("<red>Team <yellow>" + team + " <red>does not exist! Please create it first."));
            return;
        }

        Team assign = Friendlyfire.teams.get(team);
        FFPlayer FFtarget = Friendlyfire.playerList.get(target.getPlayer().getName());
        assign.addPlayer(FFtarget);
        player.sendMessage(MM.deserialize("<gray>Successfully assigned player <yellow>" + target.getPlayer().getName() + " <gray>to team <yellow>" + assign.getName() + "<gray>."));
    }

    @Subcommand("remove")
    @CommandCompletion("@players")
    public void remove(Player player, OnlinePlayer target) {
        if (Friendlyfire.playerList.get(target.getPlayer().getName()) == null) {
            player.sendMessage(MM.deserialize("<red>Couldn't find player <yellow>" + target.getPlayer().getName() + "<red>!"));
            return;
        }

        FFPlayer FFtarget = Friendlyfire.playerList.get(target.getPlayer().getName());
        if (FFtarget.getTeam() == null) {
            player.sendMessage(MM.deserialize("<red>Player <yellow>" + target.getPlayer().getName() + " <red>is not in a team!"));
            return;
        }
        FFtarget.getTeam().removePlayer(FFtarget);
        player.sendMessage(MM.deserialize("<gray>Successfully removed player <yellow>" + target.getPlayer().getName() + " <gray>from their team."));
    }

    @Subcommand("radius")
    public void radius(Player player, @Single String team) {
        int radius = 15;

        if (Friendlyfire.teams.get(team) == null) {
            player.sendMessage(MM.deserialize("<red>Team <yellow>" + team + " <red>does not exist! Please create it first."));
            return;
        }

        Bukkit.getOnlinePlayers().forEach(target -> {
            double distance = player.getLocation().distance(target.getLocation());
            if (distance < radius) {
                FFPlayer FFtarget = Friendlyfire.playerList.get(target.getName());
                if (FFtarget == null) {
                    player.sendMessage(MM.deserialize("<red>Couldn't find player <yellow>" + target.getName() + "<red>!"));
                    return;
                }

                Team assign = Friendlyfire.teams.get(team);
                assign.addPlayer(FFtarget);
                player.sendMessage(MM.deserialize("<gray>Successfully added player <yellow>" + target.getName() + " <gray>to team <yellow>" + assign.getName() + " <gray>!"));
            }
        });
    }

    @Subcommand("list")
    public void onList(Player player, @Single String name) {
        Team team = Friendlyfire.teams.get(name);
        if (team == null) {
            player.sendMessage(MM.deserialize("<red>Team <yellow>" + name + " <red>does not exist! Please create it first."));
            return;
        }

        if (team.getPlayers().isEmpty()) {
            player.sendMessage(MM.deserialize("<red>Team <yellow>" + name + " <red>is empty."));
            return;
        }

        player.sendMessage(MM.deserialize("<gray>Players in team <yellow>" + team.getName() + "<gray>:"));
        team.getPlayers().forEach(member -> {
            player.sendMessage(MM.deserialize("<gray>- <yellow>" + member.getPlayer().getName()));
        });
    }

    @Subcommand("listteams")
    public void onListTeams(Player player) {
        if (Friendlyfire.teams.isEmpty()) {
            player.sendMessage(MM.deserialize("<red>There are no registered teams available."));
            return;
        }

        player.sendMessage(MM.deserialize("<gray>Registered teams:"));
        Friendlyfire.teams.forEach((name, team) -> {
            player.sendMessage(MM.deserialize("<gray>- <yellow>" + name));
        });
    }

    @Subcommand("friendlyfire")
    public void friendlyFire(Player player, @Single String name, boolean value) {
        Team team = Friendlyfire.teams.get(name);
        if (team == null) {
            player.sendMessage(MM.deserialize("<red>Team <yellow>" + name + " <red>does not exist! Please create it first."));
            return;
        }

        team.setFriendlyFire(value);
        player.sendMessage(MM.deserialize("<gray>Set friendly-fire status for team <yellow>" + team.getName() + " <gray>to <yellow>" + value));
    }

    @Subcommand("clear")
    public void clear(Player player, @Single String name) {
        Team team = Friendlyfire.teams.get(name);
        if (team == null) {
            player.sendMessage(MM.deserialize("<red>Team <yellow>" + name + " <red>does not exist! Please create it first."));
            return;
        }

        List<FFPlayer> playersToRemove = new ArrayList<>(team.getPlayers());
        playersToRemove.forEach(team::removePlayer);
        player.sendMessage(MM.deserialize("<gray>Cleared team <yellow>" + team.getName() + "<gray>!"));
    }

    @Subcommand("nametags")
    public void nametags(Player player, boolean value) {
        if (value) {
            Friendlyfire.teams.forEach((name, team) -> {
                team.getPlayers().forEach(member -> {
                    Bukkit.dispatchCommand(
                            Bukkit.getConsoleSender(),
                            "tab player " + member.getPlayer().getName() + " abovename " + team.getName());
                });
            });
        } else {
            Friendlyfire.teams.forEach((name, team) -> {
                team.getPlayers().forEach(member -> {
                    Bukkit.dispatchCommand(
                            Bukkit.getConsoleSender(),
                            "tab player " + member.getPlayer().getName() + " abovename  ");
                });
            });
        }
        player.sendMessage(MM.deserialize("<gray>Set the global nametag status to <yellow>" + value + "<gray>."));
    }

    @Subcommand("assignall")
    public void assignAll(Player player, @Single String name) {
        Team team = Friendlyfire.teams.get(name);
        if (team == null) {
            player.sendMessage(MM.deserialize("<red>Team <yellow>" + name + " <red>does not exist! Please create it first."));
            return;
        }

        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
            FFPlayer FFonline = Friendlyfire.playerList.get(onlinePlayer.getName());
            if (FFonline == null) {
                Bukkit.getLogger().info("Player " + onlinePlayer.getName() + " is null @AssignAll!");
                return;
            }
            if (FFonline.getTeam() == null) {
                team.addPlayer(FFonline);
                player.sendMessage(MM.deserialize("<gray>Added player <yellow>" + onlinePlayer.getName() + " <gray>to team <yellow>" + team.getName() + "<gray>."));
            }
        });
    }
}
