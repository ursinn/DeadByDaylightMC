package noahnok.dbdl.files.commands;

import noahnok.dbdl.files.DeadByDaylight;
import noahnok.dbdl.files.game.DGame;
import noahnok.dbdl.files.game.STATUS;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommands implements CommandExecutor {

    private final DeadByDaylight main;

    public MainCommands(DeadByDaylight main) {
        this.main = main;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(main.prefix + "Only players can use this command!");
            return true;
        }

        Player p = (Player) commandSender;
        if (args.length == 0) {
            p.sendMessage(main.prefix + main.getMessageUtils().color(
                    "&6DeadByDaylight &7recreated for Minecraft (1.12+)"));
            p.sendMessage(main.prefix + main.getMessageUtils().color(
                    "Subcommands: forcestart,forceend,mysql"));
            p.sendMessage(main.prefix + main.getMessageUtils().color(
                    "&6forcestart &8>&7> Used when you are in a game lobby!"));
            p.sendMessage(main.prefix + main.getMessageUtils().color(
                    "&6forceend &8>&7> Used when you are in a game!"));
            p.sendMessage(main.prefix + main.getMessageUtils().color(
                    "&6mysql [connect] &8>&7> Shows the current MySQL status " +
                            "(Typing 'connect' will allow you to try and reconnect if" +
                            " your MySQL details in the config are invalid!"));
            return true;
        }

        switch (args[0]) {
            case "forcestart":
                DGame game = main.getGameManager().getGamePlayerIsIn(p);
                if (game == null || game.getStatus() != STATUS.WAITING) {
                    p.sendMessage("You must be in a game to do this!");
                    break;
                }

                if (game.getHunted().isEmpty()) {
                    p.sendMessage("Your game must have at least one survivor to start!");
                    break;
                }

                if (game.getHunters().isEmpty()) {
                    p.sendMessage("Your game must have at least one hunter to start!");
                    break;
                }
                main.getGameManager().forceStartGame(game);
                break;

            case "forceend":
                DGame gameend = main.getGameManager().getGamePlayerIsIn(p);
                if (gameend == null || gameend.getStatus() == STATUS.WAITING) {
                    p.sendMessage("You must be in a game to do this!");
                    break;
                }

                main.getGameManager().endGame(gameend);
                break;

            case "mysql":
                if (args.length > 1) {
                    mySQLSwitch(p, args[1]);
                    break;
                }

                mySQLSwitch(p, "");
                break;

            case "running":
                for (DGame runningGame : main.getGameManager().getGames()) {
                    p.sendMessage("Arena: " + runningGame.getArena().getId() + " Players: "
                            + runningGame.getPlayers().size() + " GameID: " + runningGame.getId());
                }
                break;

            default:
                p.sendMessage("Couldn't find subcommand: " + args[0]);
        }
        return true;
    }

    public void mySQLSwitch(CommandSender sender, String arg) {
        switch (arg) {
            case "connect":
                sender.sendMessage("Checking connection to MySQL!");
                if (main.getSqlManager().reInitConnection()) {
                    sender.sendMessage("Connected to MySQL");
                    break;
                }

                sender.sendMessage("Failed to connect! Check console!");
                break;

            default:
                sender.sendMessage("Connected to MySQL Server: " + main.getToggles().usingSQL);
                sender.sendMessage("Subcommands: connect");
                break;
        }

    }
}
