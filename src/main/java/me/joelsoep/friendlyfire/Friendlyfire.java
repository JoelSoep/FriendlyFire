package me.joelsoep.friendlyfire;

import co.aikar.commands.PaperCommandManager;
import com.google.common.collect.ImmutableList;
import me.joelsoep.friendlyfire.command.FFCommand;
import me.joelsoep.friendlyfire.listener.EntityDamageByEntityListener;
import me.joelsoep.friendlyfire.listener.PlayerJoinListener;
import me.joelsoep.friendlyfire.listener.PlayerQuitListener;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.concurrent.Immutable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class Friendlyfire extends JavaPlugin {

    public static final HashMap<String, FFPlayer> playerList = new HashMap<>();
    public static final HashMap<String, Team> teams = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        PaperCommandManager manager = new PaperCommandManager(this);
        manager.registerCommand(new FFCommand());

        getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);

      //  manager.getCommandCompletions().registerCompletion("teams", completion -> {
       //   List<String> names = new ArrayList<>();
       //   teams.forEach((name, team) -> names.add(name));
       //   return names;
      //  });

    }
}
