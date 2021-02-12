package noahnok.dbdl.files.generalEvents;

import noahnok.dbdl.files.DeadByDaylight;
import noahnok.dbdl.files.player.DPlayer;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;


public class GenericEvents implements Listener {

    private final DeadByDaylight main;

    public GenericEvents(DeadByDaylight main) {
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        main.getdPlayerManager().loadDPlayer(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        main.getdPlayerManager().savePlayer(e.getPlayer().getUniqueId());
    }


    // Not cancelling. Just clearing recipients so it doesn't cause problems with other plugins.
    @EventHandler
    public void divertGameChat(AsyncPlayerChatEvent e) {
        DPlayer player = main.getdPlayerManager().getPlayer(e.getPlayer().getUniqueId());
        if (player.getCurrentGame() != null) {
            if (e.getMessage().startsWith("!")) {
                e.setMessage(e.getMessage().substring(1));
                return;
            }
            e.getRecipients().clear();
            player.getCurrentGame().getPlayers().forEach(dplayer -> e.getRecipients().add(dplayer.getPlayer()));
            e.setFormat(main.prefix + (player.isHunter() ? ChatColor.DARK_RED : ChatColor.GOLD) + player.getName() + ChatColor.translateAlternateColorCodes('&', " &8>&7> &f") + e.getMessage());
        }
    }
}
