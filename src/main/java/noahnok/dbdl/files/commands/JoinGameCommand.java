package noahnok.dbdl.files.commands;

import noahnok.dbdl.files.DeadByDaylight;
import noahnok.dbdl.files.utils.CustomHolder;
import noahnok.dbdl.files.utils.Icon;
import noahnok.dbdl.files.utils.builders.Builders;
import noahnok.dbdl.files.utils.builders.InventoryBuilder;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinGameCommand implements CommandExecutor {

    private final DeadByDaylight main;

    private final InventoryBuilder inventoryBuilder = new InventoryBuilder();

    private final Builders itemBuilder = new Builders();
    private CustomHolder joinGameInv;

    public JoinGameCommand(DeadByDaylight main) {
        this.main = main;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(main.prefix + "Only players can use this command!");
            return true;
        }

        Player player = (Player) commandSender;
        if (main.getGameManager().getGamePlayerIsIn(player) != null) {
            player.sendMessage(main.prefix + "You are already in a game!");
            return true;
        }

        player.openInventory(joinGameInv.getInventory());
        return true;
    }

    public void setUpjoinGameInv() {
        joinGameInv = inventoryBuilder.createNew("Join Game", 27);
        Icon hunted = inventoryBuilder.createIcon(
                itemBuilder.getNewBuilder(Material.BONE_BLOCK).build(), "Play as a survivor!");
        hunted.addLore("May have longer waiting times!");
        Icon hunter = inventoryBuilder.createIcon(
                itemBuilder.getNewBuilder(Material.COAL_BLOCK).build(), "Play as a hunter!");
        hunter.addLore("Often results in getting into a game quicker!");

        hunted.addClickAction((Player p) -> {
            p.closeInventory();
            if (!main.getMatchMaking().addToMatchmaking(p, "HUNTED")) {
                main.getMatchMaking().addPlayerToMatchMakingLoop(p, "HUNTED");
            }
        });

        hunter.addClickAction((Player p) -> {
            p.closeInventory();
            if (!main.getMatchMaking().addToMatchmaking(p, "HUNTER")) {
                main.getMatchMaking().addPlayerToMatchMakingLoop(p, "HUNTER");
            }
        });

        joinGameInv.setIcon(11, hunted);
        joinGameInv.setIcon(15, hunter);
    }

}
