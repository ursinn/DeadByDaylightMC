package noahnok.DBDL.files.utils;

import noahnok.DBDL.files.DeadByDaylight;
import noahnok.DBDL.files.player.DPlayer;
import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;

public class MessageUtils {

    private DeadByDaylight main;

    private Map<String, String> messages = new HashMap<>();

    public MessageUtils(DeadByDaylight main) {
        this.main = main;
        //loadMessages();
    }

    public void sendMessage(String message, DPlayer p) {
        p.getPlayer().sendMessage(replaceVariables(getMessage(message), p));
    }

    public String getMessage(String message) {
        return messages.get(message);
    }

    private String replaceVariables(String message, DPlayer p) {
        if (message.contains("%player%")) {
            message.replace("%player%", p.getName());
        }

        if (message.contains("%arena_id%")) {
            message.replace("%arena_id%", p.getCurrentGame().getArena().getID());
        }

        if (message.contains("%bloodpoints%")) {
            message.replace("%bloodpoints%", p.getGameScore() + "");
        }

        return message;
    }

    public void loadMessages() {
        if (main.getMessagesConfig().getConfig().getConfigurationSection("messages").getKeys(false).size() == 0 || main.getMessagesConfig().getConfig().getConfigurationSection("messages").getKeys(false) == null) {
            main.getMessagesConfig().reloadConfig();

        }
        for (String key : main.getMessagesConfig().getConfig().getConfigurationSection("messages").getKeys(false)) {
            try {
                messages.put(key, ChatColor.translateAlternateColorCodes('&', main.getMessagesConfig().getConfig().getString("messages." + key)));
            } catch (NullPointerException e) {
                messages.put(key, ChatColor.RED + "Error loading message: " + key + " Please check your lang.yml and then reload the messages!");
            }
        }

    }

    public String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
