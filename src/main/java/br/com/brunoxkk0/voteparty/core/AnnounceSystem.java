package br.com.brunoxkk0.voteparty.core;

import br.com.brunoxkk0.voteparty.VoteParty;
import br.com.brunoxkk0.voteparty.core.data.ConfigData;
import br.com.brunoxkk0.voteparty.core.data.VoteData;
import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;


public class AnnounceSystem {

    public static void start(){

        VoteParty.getInstance().getService().scheduleAtFixedRate(
                run(),
                0,
                ConfigData.getInstance().getBossBarWarnTime(),
                TimeUnit.MINUTES
        );

    }


    public static Runnable run(){
        return run(null);
    }

    public static Runnable run(Player player){
        return () -> {

            if(Bukkit.getOnlinePlayers().isEmpty()) return;

            ConfigData configData = ConfigData.getInstance();

            BossBar bossBar = Bukkit.createBossBar(
                    configData.getBossBarMessage()
                            .replace("%m", "" + VoteData.getInstance().getMissingVoteCount())
                            .replace("%c", "" + VoteData.getInstance().getCurrentVoteCount())
                            .replace("%r", "" + VoteData.getInstance().getRequiredVotesCount())
                            .replace("&",  "\u00a7"),
                    configData.getBossBarColor(),
                    configData.getBossBarStyle()
            );

            bossBar.setProgress((VoteHandler.getVotePercentage(configData.getBossBarCounterType()) / 100.0F));

            if(player != null){
                bossBar.addPlayer(player);
            }else {
                Bukkit.getOnlinePlayers().forEach(bossBar::addPlayer);
            }

            bossBar.setVisible(true);

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                VoteParty.getInstance().getLoggerHelper().error(e.getMessage());
            }

            bossBar.setVisible(false);
            bossBar.removeAll();

        };
    }

}
