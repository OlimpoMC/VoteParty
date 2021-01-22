package br.com.brunoxkk0.voteparty;

import br.com.brunoxkk0.servercore.api.ConfigAPI;
import br.com.brunoxkk0.servercore.api.LoggerHelper;
import br.com.brunoxkk0.voteparty.core.BarHandlerThread;
import br.com.brunoxkk0.voteparty.core.VoteHandler;
import org.bukkit.Bukkit;

import org.bukkit.plugin.java.JavaPlugin;

public class VoteParty extends JavaPlugin {

    private static VoteParty instance;

    private final ConfigAPI configAPI = new ConfigAPI(this, "config.yml", true, false);

    private LoggerHelper loggerHelper;
    private BarHandlerThread barHandlerThread;
    private VoteHandler voteHandler;

    public LoggerHelper getLoggerHelper() {
        return this.loggerHelper;
    }

    public static VoteParty getInstance() {
        return instance;
    }

    public ConfigAPI getConfigAPI() {
        return this.configAPI;
    }

    public BarHandlerThread getBarHandlerThread() {
        return this.barHandlerThread;
    }

    public VoteHandler getVoteHandler() {
        return this.voteHandler;
    }


    public void onEnable() {
        instance = this;

        loggerHelper = new LoggerHelper(this);

        loggerHelper.info("Loading VoteHandler...");

        voteHandler = new VoteHandler();
        voteHandler.setup();

        loggerHelper.info("Registering events...");
        Bukkit.getPluginManager().registerEvents(this.voteHandler, this);

        loggerHelper.info("Loading BarHandlerThread...");

        barHandlerThread = new BarHandlerThread();
        barHandlerThread.setup();
    }


    public void onDisable() {
        loggerHelper.info("Shutting down systems and saving data.");
        voteHandler.save();

        if (!this.barHandlerThread.getWarn().isInterrupted())
            this.barHandlerThread.getWarn().interrupt();
    }
}
