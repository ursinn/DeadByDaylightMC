package noahnok.dbdl.files.commands;

import noahnok.dbdl.files.DeadByDaylight;
import noahnok.dbdl.files.game.DGame;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCommand implements CommandExecutor {

    private final DeadByDaylight main;

    public LeaveCommand(DeadByDaylight main) {
        this.main = main;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(main.prefix + "Only players can use this command!");
            return true;
        }

        Player player = (Player) commandSender;
        if (main.getMatchMaking().matchMakingLoop.containsKey(player.getUniqueId())) {
            main.getMatchMaking().removePlayerFromMatchMakingLoop(player);
            player.sendMessage(main.prefix + "You have left the queue!");
            return true;
        }

        DGame game = main.getGameManager().getGamePlayerIsIn(player);
        if (game != null) {
            main.getGameManager().removePlayerFromGame(player, game);

            //Remove player
            return true;
        }

        player.sendMessage(main.prefix + "You aren't in a game or queue!");
        return true;
    }
}
