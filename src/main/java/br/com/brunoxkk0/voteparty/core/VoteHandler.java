package br.com.brunoxkk0.voteparty.core;


import br.com.brunoxkk0.voteparty.VoteParty;
import br.com.brunoxkk0.voteparty.core.data.VoteData;
import com.vexsoftware.votifier.model.VotifierEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.concurrent.TimeUnit;


public class VoteHandler implements Listener {

    private static final VoteData voteData = VoteData.getInstance();

    public static void load(){
        VoteParty.getInstance().getService().scheduleAtFixedRate(() -> VoteData.getInstance().save(), 0, 1, TimeUnit.MINUTES);
    }

    public static float getVotePercentage(CounterType type) {

        if (type == CounterType.INVERTED) {
            return Math.min(100.0F, Math.max(100.0F - voteData.getCurrentVoteCount() * 100.0F / voteData.getRequiredVotesCount(), 0.0F));
        }

        return Math.min(100.0F, Math.max(voteData.getCurrentVoteCount() * 100.0F / voteData.getRequiredVotesCount(), 0.0F));

    }


    public synchronized void process(Player player) {

        voteData.addVote(this);

        if (player != null) {
            Bukkit.getScheduler().runTaskAsynchronously(
                    VoteParty.getInstance(),
                    AnnounceSystem.run(player)
            );
        }

    }

    public void doReward(){

        Bukkit.broadcastMessage(voteData.getRewardMessage().replace("&", "§"));

        Bukkit.getOnlinePlayers().forEach(player -> {

            voteData.getRewardCommands().forEach(command -> {
                if (command.startsWith("/")) { command = command.substring(1); }
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%p", player.getName()));
            });

            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);

        });

    }



    @EventHandler
    public synchronized void onVote(VotifierEvent event) {

        OfflinePlayer player = Bukkit.getOfflinePlayer(event.getVote().getUsername());

        if (player != null && player.hasPlayedBefore()) {

            if (player.isOnline()) {

                process(player.getPlayer());
                return;

            }
            VoteParty.getInstance().getLoggerHelper().info("Player " + event.getVote().getUsername() + " voted for the server but was offline.");
        }

        VoteParty.getInstance().getLoggerHelper().info("The player" + event.getVote().getUsername() + " never had played in this server before.");
    }

}