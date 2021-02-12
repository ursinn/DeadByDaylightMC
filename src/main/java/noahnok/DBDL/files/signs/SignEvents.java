package noahnok.dbdl.files.signs;

import noahnok.dbdl.files.DeadByDaylight;
import noahnok.dbdl.files.game.DGame;
import noahnok.dbdl.files.utils.ClickAction;
import noahnok.dbdl.files.utils.CustomHolder;
import noahnok.dbdl.files.utils.Icon;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SignEvents implements Listener {

    private final DeadByDaylight main;
    private final String signLine = ChatColor.translateAlternateColorCodes('&', "&8[&7DBDL&8]");

    public SignEvents(DeadByDaylight main) {
        this.main = main;
    }

    @EventHandler
    public void onSignCreate(SignChangeEvent e) {
        if (e.getLine(0).equalsIgnoreCase("[dbdl]")) {
            if (e.getLine(1).equalsIgnoreCase("join")) {
                e.getPlayer().sendMessage("Created game join sign!");

                e.setLine(0, signLine);
                e.setLine(1, ChatColor.RED + "Searching");
                DSign sign = new DSign(e.getBlock());
                main.getSignManager().getSigns().add(sign);

                sign.firstPlace();
            }
        }
    }

    @EventHandler
    public void signBreakEvent(BlockBreakEvent e) {
        if (e.getBlock().getType().equals(Material.SIGN) || e.getBlock().getType().equals(Material.WALL_SIGN)) {
            if (((Sign) e.getBlock().getState()).getLine(0).equals(signLine)) {
                if (e.getPlayer().hasPermission("dbdl.sign.break")) {

                } else {
                    e.getPlayer().sendMessage("You cannot break this sign!");
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void signInteract(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            if (e.getClickedBlock().getType().equals(Material.SIGN) || e.getClickedBlock().getType().equals(Material.WALL_SIGN)) {
                if (main.getSignManager().getSign(e.getClickedBlock()) != null) {
                    Sign sign = main.getSignManager().getSign(e.getClickedBlock()).getSign();
                    if (sign.getLine(0).equals(signLine)) {
                        if (main.getSignManager().getSign(e.getClickedBlock()).getGame() != null) {
                            if (main.getSignManager().getSign(e.getClickedBlock()).getGame().equals(main.getGameManager().getGamePlayerIsIn(e.getPlayer()))) {
                                e.getPlayer().sendMessage("You are already in a game!");
                                return;
                            }
                            e.getPlayer().openInventory(joinGameInvFromSign(main.getSignManager().getSign(e.getClickedBlock()).getGame()));
                        }
                    }
                }
            }
        }

    }

    private Inventory joinGameInvFromSign(final DGame game) {
        CustomHolder newInv = new CustomHolder(27, "Join game: " + game.getArena().getID());

        Icon hunted = new Icon(new ItemStack(Material.BONE_BLOCK, 1), "Play as a survivor!");

        Icon hunter = new Icon(new ItemStack(Material.COAL_BLOCK, 1), "Play as a hunter!");

        hunted.addClickAction(new ClickAction() {
            public void execute(Player p) {
                p.closeInventory();
                main.getGameManager().joinPlayerToGame(p, game, "HUNTED");
            }
        });

        hunter.addClickAction(new ClickAction() {
            public void execute(Player p) {
                p.closeInventory();
                main.getGameManager().joinPlayerToGame(p, game, "HUNTER");
            }
        });

        newInv.setIcon(11, hunted);
        newInv.setIcon(15, hunter);

        return newInv.getInventory();
    }
}
