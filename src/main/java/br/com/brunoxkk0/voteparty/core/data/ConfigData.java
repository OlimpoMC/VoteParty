package br.com.brunoxkk0.voteparty.core.data;

import br.com.brunoxkk0.voteparty.VoteParty;
import br.com.brunoxkk0.voteparty.core.CounterType;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;

public class ConfigData {

    private static ConfigData instance;

    public static ConfigData getInstance() {
        return (instance != null) ? instance : new ConfigData();
    }

    private final String        BossBarMessage;
    private final BarColor      BossBarColor;
    private final BarStyle      BossBarStyle;
    private final CounterType   BossBarCounterType;
    private final int           BossBarWarnTime;

    private ConfigData(){

        instance = this;

        BossBarMessage      =   VoteParty.getConfigAPI().getString("bar_format");
        BossBarColor        =   barColorFromString(VoteParty.getConfigAPI().getString("bar_color"));
        BossBarStyle        =   barStyleFromString(VoteParty.getConfigAPI().getString("bar_style"));
        BossBarCounterType  =   CounterType.getByName(VoteParty.getConfigAPI().getString("counter_type"));
        BossBarWarnTime      =  VoteParty.getConfigAPI().getInt("bar_warn_timer");

    }

    private BarColor barColorFromString(String color) {
        switch (color.toUpperCase()) {

            case "BLUE":    return BarColor.BLUE;
            case "GREEN":   return BarColor.GREEN;
            case "PURPLE":  return BarColor.PURPLE;
            case "RED":     return BarColor.RED;
            case "WHITE":   return BarColor.WHITE;
            case "YELLOW":  return BarColor.YELLOW;

            default: return BarColor.PINK;

        }
    }


    private BarStyle barStyleFromString(String Style) {

        switch (Style.toUpperCase()) {
            case "SEGMENTED_6":     return BarStyle.SEGMENTED_6;
            case "SEGMENTED_10":    return BarStyle.SEGMENTED_10;
            case "SEGMENTED_12":    return BarStyle.SEGMENTED_12;
            case "SEGMENTED_20":    return BarStyle.SEGMENTED_20;
            default:                return BarStyle.SOLID;
        }

    }

    public BarColor getBossBarColor() {
        return BossBarColor;
    }

    public BarStyle getBossBarStyle() {
        return BossBarStyle;
    }

    public CounterType getBossBarCounterType() {
        return BossBarCounterType;
    }

    public int getBossBarWarnTime() {
        return BossBarWarnTime;
    }

    public String getBossBarMessage() {
        return BossBarMessage;
    }

}
