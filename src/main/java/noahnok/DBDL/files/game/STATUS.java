package noahnok.DBDL.files.game;

import org.bukkit.ChatColor;

public enum STATUS {
    WAITING("&6Waiting"), STARTING("&2Starting"), INGAME("&aIn-game"), ENDING("&cEnding"), RESTARTING("&4Restarting");

    private String text;

    STATUS(String s) {
        this.text = s;
    }

    public String text(){
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
