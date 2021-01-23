package br.com.brunoxkk0.voteparty.core.data;

import br.com.brunoxkk0.servercore.api.ConfigAPI;
import br.com.brunoxkk0.voteparty.VoteParty;
import br.com.brunoxkk0.voteparty.core.VoteHandler;

import java.util.List;

public class VoteData {

    private static VoteData instance;

    public static VoteData getInstance() {
        return (instance != null) ? instance : new VoteData();
    }

    private int                 currentVoteCount;
    private final int           requiredVotesCount;
    private final List<String>  rewardCommands;
    private final String        rewardMessage;

    private boolean isDirty = false;

    private VoteData() {

        instance = this;

        currentVoteCount    = VoteParty.getConfigAPI().getInt("votes_current");
        requiredVotesCount  = VoteParty.getConfigAPI().getInt("votes_required");
        rewardCommands      = VoteParty.getConfigAPI().getStringList("reward_commands");
        rewardMessage       = VoteParty.getConfigAPI().getString("reward_message");

    }

    public void addVote(VoteHandler voteHandler){

        currentVoteCount++;

        if(currentVoteCount >= requiredVotesCount){
            voteHandler.doReward();
            currentVoteCount = 0;
        }

        isDirty = true;
    }

    public int getCurrentVoteCount() {
        return currentVoteCount;
    }

    public int getRequiredVotesCount() {
        return requiredVotesCount;
    }

    public List<String> getRewardCommands() {
        return rewardCommands;
    }

    public String getRewardMessage() {
        return rewardMessage;
    }

    public int getMissingVoteCount(){
        return getRequiredVotesCount() - getCurrentVoteCount();
    }

    public void save(){

        if(!isDirty) return;

        ConfigAPI configAPI = VoteParty.getConfigAPI();
        configAPI.setValue("votes_current", currentVoteCount);
        configAPI.save();

        isDirty = false;

    }

}
