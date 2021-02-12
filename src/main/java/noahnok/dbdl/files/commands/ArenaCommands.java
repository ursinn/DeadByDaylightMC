package noahnok.dbdl.files.commands;

import noahnok.dbdl.files.DeadByDaylight;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArenaCommands implements CommandExecutor {

    private final DeadByDaylight main;
    private final String usageMessage;

    public ArenaCommands(DeadByDaylight main) {
        this.main = main;
        this.usageMessage = "Please use /arena <create/delete/edit/manage/list>";
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(main.prefix + "Only players can use this command!");
            return true;
        }

        Player player = (Player) commandSender;
        if (args.length == 0) {
            player.sendMessage(usageMessage);
            return true;
        }

        // Arena Create command
        if (args[0].equalsIgnoreCase("create")) {
            if (args.length == 1) {
                player.sendMessage("Please provide an arena name(ID)");
                return true;
            }

            if (main.getArenaManager().createArena(args[1])) {
                player.sendMessage("Arena: " + args[1] + " created!");
                return true;
            }

            player.sendMessage("An arena with that name already exists!");
            return true;
        }

        // Arena List command
        if (args[0].equalsIgnoreCase("list")) {
            player.openInventory(main.getArenaInvManager().showArenaList());
            return true;
        }

        // Arena Delete command
        if (args[0].equalsIgnoreCase("delete")) {
            if (args.length == 1) {
                player.sendMessage("Please provide an arena name(ID)");
                return true;
            }

            if (main.getArenaManager().removeArena(args[1])) {
                player.sendMessage("Arena: " + args[1] + " was deleted!");
                return true;
            }

            player.sendMessage("Failed to find/delete an arena with that name(ID)!");
            return true;
        }

        // Arena Edit command
        if (args[0].equalsIgnoreCase("edit")) {
            if (args.length == 1) {
                player.sendMessage("Please provide an arena name(ID)");
                return true;
            }

            main.getArenaEditor().toggleEditing(player, args[1]);
            return true;
        }

        // Arena SetLobby command
        if (args[0].equalsIgnoreCase("setlobby")) {
            if (args.length == 1) {
                player.sendMessage("Please provide an arena name(ID)");
                return true;
            }

            if (main.getArenaManager().isArena(args[1]) != null) {
                main.getArenaManager().isArena(args[1]).setLobbyLocation(player.getLocation());
                player.sendMessage("Set arena: " + args[1] + "'s lobby location");
                return true;
            }

            player.sendMessage("That's not a valid arena name(ID)!");
            return true;
        }

        // Arema Manage command
        if (args[0].equalsIgnoreCase("manage")) {
            player.openInventory(main.getArenaInvManager().showMainPage());
            return true;
        }

        player.sendMessage(usageMessage);
        return true;
    }
}
