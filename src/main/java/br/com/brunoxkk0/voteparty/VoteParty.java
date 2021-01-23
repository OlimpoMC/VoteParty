package br.com.brunoxkk0.voteparty;

import br.com.brunoxkk0.servercore.api.ConfigAPI;
import br.com.brunoxkk0.servercore.api.LoggerHelper;
import br.com.brunoxkk0.voteparty.core.AnnounceSystem;
import br.com.brunoxkk0.voteparty.core.data.ConfigData;
import br.com.brunoxkk0.voteparty.core.VoteHandler;
import br.com.brunoxkk0.voteparty.core.data.VoteData;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.bukkit.Bukkit;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class VoteParty extends JavaPlugin {

    private final ScheduledExecutorService service = Executors.newScheduledThreadPool(5, new ThreadFactoryBuilder().setDaemon(true).build());

    private static VoteParty    instance;
    private static ConfigAPI    configAPI;
    private static LoggerHelper loggerHelper;

    private VoteHandler voteHandler;

    public VoteHandler getVoteHandler() {
        return voteHandler;
    }

    public LoggerHelper getLoggerHelper() {
        return loggerHelper;
    }

    public static VoteParty getInstance() {
        return instance;
    }

    public static ConfigAPI getConfigAPI() {
        return configAPI;
    }

    public ScheduledExecutorService getService() {
        return service;
    }

    public void onEnable() {
        instance = this;

        loggerHelper    = new LoggerHelper(this);
        configAPI       = new ConfigAPI(this, "config.yml", true, false);

        loggerHelper.info("Loading config data.");
        ConfigData.getInstance();

        voteHandler = new VoteHandler();
        VoteHandler.load();

        loggerHelper.info("Registering events...");
        Bukkit.getPluginManager().registerEvents(voteHandler, this);

        loggerHelper.info("Starting announce system...");
        AnnounceSystem.start();

    }


    public void onDisable() {
        loggerHelper.info("Shutting down systems and saving data.");
        VoteData.getInstance().save();
    }
}
