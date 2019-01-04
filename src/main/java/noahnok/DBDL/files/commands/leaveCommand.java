package noahnok.DBDL.files.commands;

import noahnok.DBDL.files.DeadByDaylight;
import noahnok.DBDL.files.game.DGame;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

;

public class leaveCommand implements CommandExecutor {

    private DeadByDaylight main;

    public leaveCommand(DeadByDaylight main) {
        this.main = main;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player){
            Player player = (Player) commandSender;
            if (main.getMatchMaking().matchMakingLoop.containsKey(player.getUniqueId())){
                main.getMatchMaking().removePlayerFromMatchMakingLoop(player);
                player.sendMessage(main.prefix + "You have left the queue!");
                return true;
            }

            DGame game = main.getGameManager().getGamePlayerIsIn(player);
            if (game != null){
                main.getGameManager().removePlayerFromGame(player, game);

                //Remove player
                return true;
            }
            player.sendMessage(main.prefix + "You aren't in a game or queue!");
            return true;


        }else{
            commandSender.sendMessage(main.prefix + "This command can only be executed by a player!");
            return true;
        }
    }
}
