package noahnok.DBDL.files.game.events;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import noahnok.DBDL.files.DeadByDaylight;
import noahnok.DBDL.files.game.DGame;
import noahnok.DBDL.files.game.STATUS;
import noahnok.DBDL.files.game.generators.Generator;
import noahnok.DBDL.files.game.levers.DLever;
import noahnok.DBDL.files.player.DPlayer;
import noahnok.DBDL.files.player.PlayerStatus;
import noahnok.DBDL.files.utils.Pagenation.Page;
import noahnok.DBDL.files.utils.Pagenation.buttons.SkullItem;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;


public class MainEvents implements Listener {

    private DeadByDaylight main;

    public MainEvents(DeadByDaylight main) {
        this.main = main;
    }

    @EventHandler
    public void canMove(PlayerMoveEvent e) {
        DPlayer dPlayer = main.getdPlayerManager().getPlayer(e.getPlayer().getUniqueId());
        if (dPlayer == null) return;
        DGame game = dPlayer.getCurrentGame();
        if (game == null) return;
        if (game != null && (game.getStatus().equals(STATUS.INGAME) || game.getStatus().equals(STATUS.STARTING))) {

            if (!game.canPlayerMove(e.getPlayer().getUniqueId())) {
                Location location = e.getFrom();
                location.setPitch(e.getTo().getPitch());
                location.setYaw(e.getTo().getYaw());
                e.setTo(location);

            }
            if (game.isCanOpenGates() && game.isCanEscape()) {
                for (Location loc : game.getArena().getExitArea()) {
                    if (e.getPlayer().getLocation().getBlock().equals(loc.getBlock()) && dPlayer.getStatus().equals(PlayerStatus.HUNTED)) {

                        game.announce(e.getPlayer().getName() + " has escaped!");
                        main.getPlayerStateManager().survivorEscapes(dPlayer);

                        dPlayer.setEscapes(dPlayer.getEscapes() + 1);
                        dPlayer.addToScore(1000);
                        game.moveToEscaped(e.getPlayer().getUniqueId());


                        if (game.getStatus() == STATUS.INGAME) dPlayer.startSpectating(game);


                    }
                }

            }
        }
    }

    @EventHandler
    public void blockInteract(PlayerInteractEvent e) {

        DGame game = main.getGameManager().getGamePlayerIsIn(e.getPlayer());
        if (game != null && game.getStatus().equals(STATUS.INGAME)) {
            if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {


                DPlayer player = main.getdPlayerManager().getPlayer(e.getPlayer().getUniqueId());
                e.setCancelled(true);

                //Generator operation
                if (e.getClickedBlock().getType().equals(Material.FURNACE) && !game.isCanOpenGates()) {
                    if (player.getStatus().equals(PlayerStatus.HUNTER)) {
                        return;
                    }

                    Generator gen = game.isGenerator(e.getClickedBlock());
                    if (gen != null) {
                        if (!gen.isFinished()) {
                            gen.increment(player);
                        }
                        e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', "&7Generator is at: &6" + gen.getPercentDone() + "%")));
                    }
                }


                //Lever operation
                if (e.getClickedBlock().getType().equals(Material.LEVER) && game.isCanOpenGates()) {
                    if (player.getStatus().equals(PlayerStatus.HUNTER)) {
                        return;
                    }
                    DLever lever = game.getLeverAtBlock(e.getClickedBlock());
                    if (lever != null) {
                        if (!lever.isFinished()) {
                            lever.increment(player);

                            e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', "&7Lever is at: &6" + lever.getPercentDone() + "%")));
                        }
                    }


                }
            }
        }
    }


    @EventHandler
    public void preventGameDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            DPlayer player = main.getdPlayerManager().getPlayer(((Player) e.getEntity()).getUniqueId());
            if (player != null && player.getCurrentGame() != null && !player.isPlayerASurvivorAndAlive()) {
                e.setCancelled(true);

                //TODO Methods for states here!
            }
        }
    }


    private void handlePlayerSneak(DPlayer dplayer) {

    }


    @EventHandler
    public void sneakMenu(PlayerToggleSneakEvent e) {

        if (!e.isSneaking()) {
            DGame game = main.getGameManager().getGamePlayerIsIn(e.getPlayer());
            if (game != null && game.getStatus().equals(STATUS.INGAME)) {
                DPlayer dplayer = game.getPlayer(e.getPlayer().getUniqueId());
                if (!dplayer.isSpectating()) return;

                if (dplayer.isSpectating() && dplayer.getSpectate() != null) {
                    dplayer.spectateNext(dplayer.getSpectate());
                }
                Page page = new Page("Players", null);

                int index = 0;
                for (DPlayer player : game.getPlayers()) {
                    if (player == dplayer) continue;
                    SkullItem pageItem = new SkullItem();
                    pageItem.setDisplayName(player.getName());


                    if (dplayer != null && !player.isHunter()) {
                        if (player.isDead()) {
                            pageItem.addLore("&4&lDead/Escaped");
                        } else {
                            pageItem.addClickAction(p -> {
                                p.closeInventory();
                                dplayer.spectateNext(player);
                            });
                        }

                    } else {
                        pageItem.addLore("&f&lKiller");
                    }

                    pageItem.setSkull(player.getId());
                    page.addPageItem(pageItem, index);
                    index++;
                }
                e.getPlayer().openInventory(page.getInventory());
            }
        } else {
            DPlayer dplayer = main.getdPlayerManager().getPlayer(e.getPlayer().getUniqueId());
            if (dplayer.getCurrentGame() != null) {
                if (dplayer.getPlayerState().isCrawling()) {
                    e.setCancelled(true);
                }
            }
        }
    }


    //Put dead players into spectator
    @EventHandler
    public void huntedDies(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Firework) {
            if (e.getDamager().getCustomName().equals("DBDL-FIREWORK")) {
                e.setCancelled(true);
            }

        }

        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            DPlayer damaged = main.getdPlayerManager().getPlayer(((Player) e.getEntity()).getUniqueId());
            DPlayer damager = main.getdPlayerManager().getPlayer(((Player) e.getDamager()).getUniqueId());

            if (damaged == null || damager == null) return;

            if (damaged.getCurrentGame() != null && damager.getCurrentGame() != null && damaged.getCurrentGame().equals(damager.getCurrentGame())) {
                if (!damaged.getCurrentGame().isHunter(damaged.getId()) && !damager.getCurrentGame().isHunter(damager.getId())) {
                    e.setCancelled(true);
                }
            }
        }

        if (e.getEntity() instanceof Player) {


            Player player = (Player) e.getEntity();

            if (!(e.getFinalDamage() >= player.getHealth())) {
                DGame game = main.getGameManager().getGamePlayerIsIn(player);
                if (game != null && game.getStatus().equals(STATUS.INGAME)) {

                    e.setCancelled(true);
                    DPlayer dplayer = game.getPlayer(player.getUniqueId());
                    if (dplayer != null) {
                        main.getPlayerStateManager().survivorHit(dplayer);
                    }
                }

            } else {


                DGame game = main.getGameManager().getGamePlayerIsIn(player);
                if (game != null && game.getStatus().equals(STATUS.INGAME)) {

                    e.setCancelled(true);
                    DPlayer dplayer = game.getPlayer(player.getUniqueId());
                    if (dplayer != null) {


                        // Only applied if the Hunter has a specific perk //TODO Perks for insta kill
                        /*
                        dplayer.setStatus(PlayerStatus.DEAD);


                        //Check for spectators and move the mif there are any!
                        if (main.getdPlayerManager().hasSpectators(dplayer)){
                            DPlayer newPlayerToSpectate = null;
                            for (DPlayer toBe : game.getHunted()){
                                if (toBe.isPlayerASurvivorAndAlive() && toBe != dplayer){
                                    newPlayerToSpectate = toBe;
                                    break;
                                }
                            }

                            if (newPlayerToSpectate == null){
                                main.getGameManager().canGameEnd(game);
                                return;
                            }

                            for (DPlayer spectator : main.getdPlayerManager().getSpectators(dplayer)){
                                spectator.spectateNext(newPlayerToSpectate);
                            }
                        }
                         */


                        //Have the player start spectating


                    }


                }
            }
        }
    }


    //Handle players leaving (disconnecting) during an active game
    @EventHandler
    public void onPlayerDisconnectDuringGame(PlayerQuitEvent e) {
        DPlayer player = main.getdPlayerManager().getPlayer(e.getPlayer().getUniqueId());
        if (player != null && player.getCurrentGame() != null) {
            main.getGameManager().removePlayerFromGame(player.getPlayer(), player.getCurrentGame());

        }
    }
}
