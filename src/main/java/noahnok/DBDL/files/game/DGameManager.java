package noahnok.dbdl.files.game;

import noahnok.dbdl.files.DeadByDaylight;
import noahnok.dbdl.files.game.generators.Generator;
import noahnok.dbdl.files.game.levers.DLever;
import noahnok.dbdl.files.player.DPlayer;
import noahnok.dbdl.files.player.PlayerStatus;
import noahnok.dbdl.files.signs.DSign;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class DGameManager {

    private final DeadByDaylight main;
    private final Set<DGame> games = new HashSet<>();
    private int gamesRun = 0;

    public DGameManager(DeadByDaylight main) {
        this.main = main;
    }

    public Set<DGame> getGames() {
        return games;
    }

    public DGame getGamePlayerIsIn(Player p) {
        for (DGame game : games) {
            if (game.getPlayers().contains(main.getdPlayerManager().getPlayer(p.getUniqueId()))) {
                return game;
            }
        }

        return null;
    }

    public void joinPlayerToGame(Player p, DGame game, String playType) {
        DPlayer dPlayer = main.getdPlayerManager().getPlayer(p.getUniqueId());
        if (playType.equalsIgnoreCase("HUNTER")) {
            game.getPlayers().add(dPlayer);
            dPlayer.setStatus(PlayerStatus.HUNTER);

        } else {
            game.getPlayers().add(dPlayer);
            dPlayer.setStatus(PlayerStatus.HUNTED);

        }
        p.teleport(game.getArena().getLobbyLocation());
        game.announceJoin(p);
        p.setGameMode(GameMode.SURVIVAL);
        dPlayer.setCurrentGame(game);
        canGameStart(game, false);
        dPlayer.resetPlayerState();
    }

    public String generateGameID(DGame game) {
        gamesRun++;
        return game.getArena().getID() + "_" + game.getGamemode().getID() + "_" + gamesRun;
    }

    public void removePlayerFromGame(Player p, DGame game) {
        DPlayer dPlayer = game.getPlayer(p.getUniqueId());
        game.getPlayers().remove(dPlayer);
        dPlayer.setStatus(PlayerStatus.OUT_OF_GAME);
        game.announceLeave(p);
        dPlayer.setCurrentGame(null);
        dPlayer.stopSpectating();
        dPlayer.resetPlayerState();
        p.getActivePotionEffects().clear();

        p.teleport(Bukkit.getServer().getWorld("world").getSpawnLocation());

        if (game.totalCurrentPlayers() == 0) {
            if (game.getStatus().equals(STATUS.WAITING) || game.getStatus().equals(STATUS.STARTING)) {
                DSign sign = main.getSignManager().getSign(game);
                if (sign != null) {
                    sign.removeGame();
                    sign.update();
                }
            }
            destroyGame(game);
        }

    }

    public void destroyGame(DGame game) {

        main.getMatchMaking().removeGame(game);
        game.getArena().setInUse(false);
        game.kill();
        game = null;
    }

    public void forceStartGame(DGame game) {
        canGameStart(game, true);
    }

    public void endGame(final DGame game) {
        game.endGameSound();
        game.announce("The game has ended!");
        game.setStatus(STATUS.ENDING);

        game.sendPlayersStats();

        new BukkitRunnable() {
            public void run() {
                game.endGame();
                removeMatchBlocks(game);
                destroyGame(game);
            }
        }.runTaskLater(main, 20 * 10);
    }


    private void canGameStart(DGame game, boolean force) {
        int totalReqPlayers = game.getGamemode().getHunted() + game.getGamemode().getHunters();
        int currentPlayers = game.getPlayers().size();

        if (currentPlayers >= totalReqPlayers || force) {
            //Game can start countdown

            main.getMatchMaking().removeGameFromMatchmaking(game);

            prepareMatchBlocks(game);

            game.setStatus(STATUS.STARTING);
            Countdown cd = new Countdown(10, true, 30, game, main);
            game.setCd(cd);
            cd.start();
        }
    }

    public void canGameEnd(DGame game) {
        for (DPlayer player : game.getHunted()) {
            if (player.isHunter()) continue;
            if (!player.isDead()) return;
        }

        for (DPlayer player : game.getPlayers()) {
            Player actual = player.getPlayer();
            player.stopSpectating();
            actual.setGameMode(GameMode.CREATIVE);
            actual.getInventory().clear();
            actual.setHealth(20);
            actual.setFoodLevel(20);
            actual.setFlying(true);
            main.getPlayerStateManager().survivorHealed(player, true);
        }

        endGame(game);
    }

    public DGame createNewGame() {
        DArena arena = main.getArenaManager().getRandomArena();
        if (arena == null) {
            return null;
        }

        arena.setInUse(true);
        DGamemode mode = main.getGamemodeManager().getMode("default");
        DGame game = new DGame(arena, mode, STATUS.WAITING, main);

        return game;
    }


    private Location getRandomLocation(Set<Location> locations) {
        if (locations.size() == 1) {
            return locations.iterator().next();
        }
        int rand = ThreadLocalRandom.current().nextInt(1, locations.size()) - 1;
        int i = 0;
        for (Location loc : locations) {
            if (i == rand) {
                return loc;
            }
            i++;
        }
        return null;
    }

    private void spawnGenerators(DGame game, Set<Location> locs, DGamemode mode) {
        Set<Location> temp = new HashSet<>();
        temp.addAll(locs);
        for (int i = 0; i < mode.getMaxgenerators(); i++) {
            Location loc = getRandomLocation(temp);
            temp.remove(loc);
            Generator gen = new Generator(game, loc, main);
            game.getGenerators().add(gen);
            gen.spawn();
        }
        temp.clear();
    }

    private void spawnExitGates(DGame game) {
        for (ExitGate gate : game.getArena().getExitGateLocations()) {
            for (Location loc : gate.getLocs()) {
                loc.getBlock().setType(Material.IRON_FENCE);
            }
            gate.getCenter().getBlock().setType(Material.IRON_BLOCK);
            Location leverFowardLoc = null;
            Location leverBackwardLoc = null;
            if (gate.getFacing().equalsIgnoreCase("EAST")) {
                leverFowardLoc = gate.getCenter().clone().add(1, 0, 0);
                leverBackwardLoc = gate.getCenter().clone().subtract(1, 0, 0);
            }
            if (gate.getFacing().equalsIgnoreCase("NORTH")) {
                leverFowardLoc = gate.getCenter().clone().add(0, 0, 1);
                leverBackwardLoc = gate.getCenter().clone().subtract(0, 0, 1);
            }
            if (leverFowardLoc == null || leverBackwardLoc == null) {
                main.getLogger().severe("Woah! Somethings not quite right with your exit gates LEVERS! Their facing tag is wrong! So we haven't spawned them!");
                continue;
            }
            DLever lever = new DLever(leverFowardLoc, game, gate);
            DLever lever2 = new DLever(leverBackwardLoc, game, gate);

            gate.setLever1(lever);
            gate.setLever2(lever2);

            game.getExitGates().add(gate);
        }

    }


    public void prepareMatchBlocks(DGame game) {
        DArena arena = game.getArena();
        spawnGenerators(game, arena.getPossibleGeneratorLocations(), game.getGamemode());
        spawnExitGates(game);
    }


    public void removeMatchBlocks(DGame game) {
        for (Generator gen : game.getGenerators()) {
            gen.setGame(null);
            gen.despawn();
        }
        game.getGenerators().clear();

        if (game.getArena() != null && game.getArena().getExitGateLocations() != null) {
            for (ExitGate gate : game.getArena().getExitGateLocations()) {
                for (Location loc : gate.getLocs()) {
                    loc.getBlock().setType(Material.AIR);
                }
                gate.getLever1().kill();
                gate.getLever2().kill();
                gate.setLever1(null);
                gate.setLever2(null);

            }
        }
        game.getExitGates().clear();
    }

}
