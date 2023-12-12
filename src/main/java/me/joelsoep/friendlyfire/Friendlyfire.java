package me.joelsoep.friendlyfire;

import co.aikar.commands.PaperCommandManager;
import me.joelsoep.friendlyfire.command.FFCommand;
import me.joelsoep.friendlyfire.listener.EntityDamageByEntityListener;
import me.joelsoep.friendlyfire.listener.PlayerJoinListener;
import me.joelsoep.friendlyfire.listener.PlayerQuitListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

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

    }
    
    @Override
    public void onDisable() {
        playerList.forEach((name, player) -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tab player " + name + " abovename  ");
        });
    }
}
