package noahnok.DBDL.files.commands;

import noahnok.DBDL.files.DeadByDaylight;
import noahnok.DBDL.files.utils.CustomHolder;
import noahnok.DBDL.files.utils.Icon;
import noahnok.DBDL.files.utils.builders.Builders;
import noahnok.DBDL.files.utils.builders.InventoryBuilder;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

;

public class joinGameCommand implements CommandExecutor {

    private DeadByDaylight main;


    private InventoryBuilder inventoryBuilder = new InventoryBuilder();


    private Builders itemBuilder = new Builders();

    public joinGameCommand(DeadByDaylight main) {
        this.main = main;
    }

    private CustomHolder joinGameInv;

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(main.prefix + "Only players can use this command!");
            return true;

        }
        Player player = (Player) commandSender;
        if (args.length == 0) {
            if (main.getGameManager().getGamePlayerIsIn(player) != null){
                player.sendMessage(main.prefix + "You are already in a game!");
                return true;
            }
            player.openInventory(joinGameInv.getInventory());
            return true;
        }

        if (args.length == 1){
            return true;

        }


        return true;
    }

    public void setUpjoinGameInv(){
        joinGameInv = inventoryBuilder.createNew("Join Game", 27);
        Icon hunted = inventoryBuilder.createIcon(itemBuilder.getNewBuilder(Material.BONE_BLOCK).build(), "Play as a survivor!");
        hunted.addLore("May have longer waiting times!");
        Icon hunter = inventoryBuilder.createIcon(itemBuilder.getNewBuilder(Material.COAL_BLOCK).build(), "Play as a hunter!");
        hunter.addLore("Often results in getting into a game quicker!");

        hunted.addClickAction(p -> {
            p.closeInventory();
            if(!main.getMatchMaking().addToMatchmaking(p, "HUNTED")){
                main.getMatchMaking().addPlayerToMatchMakingLoop(p, "HUNTED");
            }

        });

        hunter.addClickAction(p -> {
            p.closeInventory();
            if(!main.getMatchMaking().addToMatchmaking(p, "HUNTER")){
                main.getMatchMaking().addPlayerToMatchMakingLoop(p, "HUNTER");
            }

        });

        joinGameInv.setIcon(11, hunted);
        joinGameInv.setIcon(15, hunter);

    }




}
