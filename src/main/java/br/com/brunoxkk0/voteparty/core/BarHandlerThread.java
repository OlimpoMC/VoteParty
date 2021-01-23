package br.com.brunoxkk0.voteparty.core;

import br.com.brunoxkk0.voteparty.VoteParty;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;


public class BarHandlerThread {

    private static Thread warn;
    private static String barMessage;
    private static BarColor color;
    private static BarStyle style;
    private static CounterType counterType;
    private static int bar_warn_timer;

    public Thread getWarn() {
        return warn;
    }


    public void setup() {

        warn = new Thread(() -> {
            while (!warn.isInterrupted()) {

                BossBar bar = Bukkit.createBossBar(barMessage.replace("%m", "" + VoteHandler.getMissingVotesCount()).replace("%c", "" + VoteHandler.getCurrentVoteCount()).replace("%r", "" + VoteHandler.getRequiredVotesCount()).replace("&", "§"), color, style, new org.bukkit.boss.BarFlag[0]);


                bar.setProgress((VoteHandler.getVotePercentage(counterType) / 100.0F));


                for (Player player : Bukkit.getOnlinePlayers()) {
                    bar.addPlayer(player);
                }


                bar.setVisible(true);


                try {
                    Thread.sleep(15000L);
                } catch (InterruptedException interruptedException) {
                }

                bar.setVisible(false);

                bar.removeAll();
                try {
                    Thread.sleep((bar_warn_timer * 60000));
                } catch (InterruptedException interruptedException) {
                }

                try {
                    VoteParty.getInstance().getVoteHandler().save();
                    VoteParty.getInstance().getLoggerHelper().info("Votes successfully saved.");
                    /*  72 */
                } catch (Exception e) {
                    VoteParty.getInstance().getLoggerHelper().error("Fail to save votes.");
                }
            }
        });

        warn.start();
    }

    public void processBar(Player player) {
        (new Thread(() -> {
            BossBar bar = Bukkit.createBossBar(barMessage.replace("%m", "" + VoteHandler.getMissingVotesCount()).replace("%c", "" + VoteHandler.getCurrentVoteCount()).replace("%r", "" + VoteHandler.getRequiredVotesCount()).replace("&", "§"), color, style, new org.bukkit.boss.BarFlag[0]);

            bar.setProgress((VoteHandler.getVotePercentage(counterType) / 100.0F));

            bar.addPlayer(player);

            bar.setVisible(true);

            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            bar.setVisible(false);

            bar.removePlayer(player);
        })).start();
    }

}
