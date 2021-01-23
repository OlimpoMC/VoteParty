package br.com.brunoxkk0.voteparty.core;

import br.com.brunoxkk0.voteparty.VoteParty;
import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;


public class AnnounceSystem {

    public Runnable run(){
        return run(null);
    }

    public Runnable run(Player player){
        return () -> {

            ConfigData configData = ConfigData.getInstance();

            BossBar bossBar = Bukkit.createBossBar(
                    configData.getBossBarMessage()
                            .replace("%m", "" + VoteHandler.getMissingVotesCount())
                            .replace("%c", "" + VoteHandler.getCurrentVoteCount())
                            .replace("%r", "" + VoteHandler.getRequiredVotesCount())
                            .replace("&", "\u00a7"),
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
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                VoteParty.getInstance().getLoggerHelper().error(e.getMessage());
            }

            bossBar.setVisible(false);
            bossBar.removeAll();

        };
    }

}
