package noahnok.DBDL.files.commands;

import noahnok.DBDL.files.DeadByDaylight;
import noahnok.DBDL.files.game.DGame;
import noahnok.DBDL.files.game.STATUS;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

;

public class MainCommands implements CommandExecutor {

    private DeadByDaylight main;

    public MainCommands(DeadByDaylight main) {
        this.main = main;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            Player p = (Player) commandSender;
            if (args.length == 0) {
                p.sendMessage(main.prefix + main.getMessageUtils().color("&6DeadByDaylight &7recreated for Minecraft (1.12+)"));
                p.sendMessage(main.prefix + main.getMessageUtils().color("Subcommands: forcestart,forceend,mysql"));
                p.sendMessage(main.prefix + main.getMessageUtils().color("&6forcestart &8>&7> Used when you are in a game lobby!"));
                p.sendMessage(main.prefix + main.getMessageUtils().color("&6forceend &8>&7> Used when you are in a game!"));
                p.sendMessage(main.prefix + main.getMessageUtils().color("&6mysql [connect] &8>&7> Shows the current MySQL status (Typing 'connect' will allow you to try and reconnect if your MySQL details in the config are invalid!"));

                return true;
            } else {

                switch (args[0]) {
                    case "forcestart":
                        DGame game = main.getGameManager().getGamePlayerIsIn(p);
                        if (game == null || !game.getStatus().equals(STATUS.WAITING)) {
                            p.sendMessage("You must be in a game to do this!");

                        } else {
                            if (game.getHunted().size() == 0) {
                                p.sendMessage("Your game must have at least one survivor to start!");
                                return true;
                            }

                            if (game.getHunters().size() == 0) {
                                p.sendMessage("Your game must have at least one hunter to start!");
                                return true;
                            }
                            main.getGameManager().forceStartGame(game);
                        }
                        break;


                    case "forceend":
                        DGame gameend = main.getGameManager().getGamePlayerIsIn(p);
                        if (gameend == null || gameend.getStatus().equals(STATUS.WAITING)) {
                            p.sendMessage("You must be in a game to do this!");

                        } else {
                            main.getGameManager().endGame(gameend);
                        }
                        break;


                    case "mysql":
                        if (args.length > 1) {
                            mySQLSwitch(p, args[1]);

                        } else {
                            mySQLSwitch(p, "");
                        }
                        break;

                    case "running":
                        for (DGame runningGame : main.getGameManager().getGames()) {
                            p.sendMessage("Arena: " + runningGame.getArena().getID() + " Players: " + runningGame.getPlayers().size() + " GameID: " + runningGame.getId());
                        }
                        break;


                    default:
                        p.sendMessage("Couldn't find subcommand: " + args[0]);
                }

            }
        } else {
            commandSender.sendMessage("This command can only be executed by a player! reee");
            return true;
        }

        return true;
    }

    public void mySQLSwitch(Player p, String arg) {
        switch (arg) {
            case "connect":
                p.sendMessage("Checking connection to MySQL!");
                if (main.getSqlManager().reInitConnection()) {
                    p.sendMessage("Connected to MySQL");
                } else {
                    p.sendMessage("Failed to connect! Check console!");
                }
                break;

            default:
                p.sendMessage("Connected to MySQL Server: " + main.getToggles().usingSQL);
                p.sendMessage("Subcommands: connect");
                break;
        }

    }
}
