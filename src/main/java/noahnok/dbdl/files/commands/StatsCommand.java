package noahnok.dbdl.files.commands;

import noahnok.dbdl.files.DeadByDaylight;
import noahnok.dbdl.files.player.DPlayer;
import noahnok.dbdl.files.utils.pagenation.Page;
import noahnok.dbdl.files.utils.pagenation.buttons.PageItem;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsCommand implements CommandExecutor {

    private final DeadByDaylight main;

    public StatsCommand(DeadByDaylight main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(main.prefix + "Only players can use this command!");
            return true;
        }

        showPlayerStats(((Player) commandSender));
        return true;
    }

    private void showPlayerStats(Player player) {
        DPlayer dplayer = main.getdPlayerManager().getPlayer(player.getUniqueId());
        Page statsPage = new Page("Your Stats", null);
        statsPage.setPageSize(3);

        PageItem wins = new PageItem().setMaterial(Material.NETHER_STAR)
                .setDisplayName("&f&lWins:").addLore("&f" + dplayer.getWins());
        PageItem losses = new PageItem().setMaterial(Material.COAL)
                .setDisplayName("&0&lLosses (Hunter):").addLore("&0" + dplayer.getLosses());
        PageItem generators = new PageItem().setMaterial(Material.FURNACE)
                .setDisplayName("&8&lGenerators").addLore("&7Fixed: " + dplayer.getGeneratorsFixed())
                .addLore("&7Blown: " + dplayer.getGeneratorsMessedup());

        statsPage.addPageItem(wins, 11);
        statsPage.addPageItem(losses, 12);
        statsPage.addPageItem(generators, 13);

        player.openInventory(statsPage.getInventory());
    }
}
