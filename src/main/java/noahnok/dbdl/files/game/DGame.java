package noahnok.dbdl.files.game;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import noahnok.dbdl.files.DeadByDaylight;
import noahnok.dbdl.files.game.generators.Generator;
import noahnok.dbdl.files.game.levers.DLever;
import noahnok.dbdl.files.player.DPlayer;
import noahnok.dbdl.files.player.PlayerStatus;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class DGame {

    private final String id;
    private final Set<DPlayer> players = new HashSet<>();
    private final Map<UUID, Boolean> allowMove = new HashMap<>();
    private final List<Generator> generators = new ArrayList<>();
    private final List<ExitGate> exitGates = new ArrayList<>();
    private DeadByDaylight main;
    private DArena arena;
    private DGamemode gamemode;
    private STATUS status;
    private boolean usesLobby;
    private Location lobbyLocation;
    private boolean customGame;
    private boolean showInPool;
    private int finishedGens;
    private boolean canOpenGates = false;
    private boolean canEscape = false;
    private Countdown cd;
    private InGameCountdown igCD;

    public DGame(DArena arena, DGamemode gamemode, STATUS status, DeadByDaylight main) {
        this.arena = arena;
        this.gamemode = gamemode;
        this.status = status;
        this.main = main;
        this.id = main.getGameManager().generateGameID(this);
    }

    public List<ExitGate> getExitGates() {
        return exitGates;
    }

    public List<Generator> getGenerators() {
        return generators;
    }

    public String getId() {
        return id;
    }

    public void moveToDead(UUID id) {
        main.getdPlayerManager().getPlayer(id).setStatus(PlayerStatus.DEAD);
    }

    public void moveToEscaped(UUID id) {
        main.getdPlayerManager().getPlayer(id).setStatus(PlayerStatus.ESCAPED);
        main.getGameManager().canGameEnd(this);
    }

    public DLever getLeverAtBlock(Block block) {
        for (ExitGate gate : exitGates) {
            if (gate.getLever1().getBlock().equals(block)) {
                return gate.getLever1();
            }
            if (gate.getLever2().getBlock().equals(block)) {
                return gate.getLever2();
            }
        }
        return null;
    }

    public int getFinishedGens() {
        return finishedGens;
    }

    public boolean isCanOpenGates() {
        return canOpenGates;
    }

    public boolean isCanEscape() {
        return canEscape;
    }

    public void setCanEscape(boolean canEscape) {
        this.canEscape = canEscape;
    }

    public void incrementGens() {
        finishedGens++;
        if (finishedGens >= 5) {
            announce("The exit gates have been powered! Go open them!");
            for (Generator gen : generators) {
                gen.complete();
            }
            canOpenGates = true;
        }
    }

    private void allowPlayerMove(UUID id) {
        allowMove.put(id, true);
    }

    private void disallowPlayerMove(UUID id) {
        allowMove.put(id, false);
    }

    public DPlayer getPlayer(UUID id) {
        for (DPlayer player : players) {
            if (player.getId().equals(id)) {
                return player;
            }
        }
        return null;
    }


    public boolean canPlayerMove(UUID id) {
        if (allowMove.get(id) == null) {
            return true;
        }
        return allowMove.get(id);
    }

    public void allowAllMove() {
        for (DPlayer player : players) {
            allowPlayerMove(player.getId());
        }
    }

    public void disallowAllMove() {
        for (DPlayer player : players) {
            disallowPlayerMove(player.getId());
        }
    }

    public Set<DPlayer> getPlayers() {
        return players;
    }

    public int totalCurrentPlayers() {
        return players.size();
    }

    public int totalPossiblePlayers() {
        return gamemode.getHunters() + gamemode.getHunted();
    }

    public InGameCountdown getIgCD() {
        return igCD;
    }

    void setIgCD(InGameCountdown igCD) {
        this.igCD = igCD;
    }

    public void teleportPlayers() {
        Set<Location> huntedspawns = new HashSet<>(arena.getPossibleHuntedSpawns());
        Set<Location> hunterspawns = new HashSet<>(arena.getPossibleHunterSpawns());
        for (DPlayer player : players) {
            if (player.getStatus().equals(PlayerStatus.HUNTED)) {
                Location loc = getRandomLocation(huntedspawns);
                huntedspawns.remove(loc);
                main.getServer().getPlayer(player.getId()).teleport(loc);
            } else {
                Location loc = getRandomLocation(hunterspawns);
                hunterspawns.remove(loc);
                main.getServer().getPlayer(player.getId()).teleport(loc);
            }
        }

    }

    private Location getRandomLocation(Set<Location> locations) {

        if (locations.size() == 1) {
            return locations.iterator().next();
        }

        main.getLogger().info("size: " + locations.size());
        int rand = ThreadLocalRandom.current().nextInt(1, locations.size()) - 1;
        main.getLogger().info("rand: " + rand);
        int i = 0;
        for (Location loc : locations) {
            if (i == rand) {

                return loc;
            }
            i++;
        }
        return locations.iterator().next();
    }

    public void endGame() {
        for (DPlayer player : players) {
            Player actual = player.getPlayer();
            actual.teleport(Bukkit.getServer().getWorld("world").getSpawnLocation());
            player.setSpectating(false);
            actual.setGameMode(GameMode.SURVIVAL);
            actual.setHealth(20);
            actual.setSaturation(20);
            actual.setFoodLevel(20);

            actual.setFlying(false);

            player.resetPlayerState();

            player.setCurrentGame(null);

            player.stopSpectating();

        }

        players.clear();
    }

    public boolean isHunter(UUID id) {
        for (DPlayer player : players) {
            if (player.getId().equals(id)) {
                if (player.getStatus().equals(PlayerStatus.HUNTER)
                        || player.getStatus().equals(PlayerStatus.CARRYING)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Generator isGenerator(Block block) {
        for (Generator gen : generators) {
            if (gen.getBlock().equals(block)) {
                return gen;
            }
        }
        return null;
    }

    public void kill() {
        this.arena.setInUse(false);
        this.arena = null;
        this.cd = null;
        this.gamemode = null;
        this.status = null;
        this.main = null;
    }


    public void setCd(Countdown cd) {
        this.cd = cd;
    }

    public DArena getArena() {
        return arena;
    }

    public DGamemode getGamemode() {
        return gamemode;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public Set<DPlayer> getHunted() {
        return players.stream().filter(player -> player.getStatus().equals(PlayerStatus.HUNTED))
                .collect(Collectors.toCollection(HashSet::new));
    }


    public Set<DPlayer> getHunters() {
        return players.stream().filter(player -> player.getStatus().equals(PlayerStatus.HUNTER) ||
                player.getStatus().equals(PlayerStatus.CARRYING)).collect(Collectors.toCollection(HashSet::new));
    }


    public void announceJoin(Player p) {
        for (DPlayer player : players) {
            Player reciever = main.getServer().getPlayer(player.getId());
            reciever.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacyText(ChatColor.GOLD + p.getName() + ChatColor.GRAY + " joined the game!"));
            reciever.sendMessage(main.prefix + ChatColor.GOLD + p.getName() + ChatColor.GRAY + " joined the game!");

        }

        p.sendMessage(main.prefix + "You joined the game!");
    }

    public void announceLeave(Player p) {
        for (DPlayer player : players) {
            Player reciever = main.getServer().getPlayer(player.getId());
            reciever.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacyText(ChatColor.GOLD + p.getName() + ChatColor.GRAY + " left the game!"));
            reciever.sendMessage(main.prefix + ChatColor.GOLD + p.getName() + ChatColor.GRAY + " left the game!");

        }

        p.sendMessage(main.prefix + "You left the game!");
    }

    public void announce(String message) {
        for (DPlayer player : players) {
            sendAB(message, player.getId());
            main.getServer().getPlayer(player.getId()).sendMessage(main.prefix + message);
        }

    }

    private void sendAB(String message, UUID id) {
        Bukkit.getServer().getPlayer(id).spigot().sendMessage(ChatMessageType.ACTION_BAR,
                TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', message)));
    }

    public double getMultiplier() {
        int playercount = players.size() - 1;
        switch (playercount) {
            case 4:
                return 1.0;
            case 3:
                return 1.25;
            case 2:
                return 1.75;
            case 1:
                return 2.5;
            default:
                return 1.0;
        }
    }

    public void sendPlayersStats() {

        for (DPlayer dplayer : players) {
            String message = "";
            switch (dplayer.getPlayerState().getEndGameState()) {
                case ESCAPED:
                    message = "You escaped the " + ChatColor.BLACK + "Hunter";
                    break;

                case DEAD:
                    message = "You " + ChatColor.BLACK + "Died";
                    break;

                case SACRIFICED:
                    message = "You were " + ChatColor.DARK_RED + "Sacrificed to the " + ChatColor.BLACK + "Entity";
                    break;

                // Always the hunter,, if not used as a fail safe!
                case NONE:
                    message = "You have displeased the " + ChatColor.BLACK + "Entity";
                    break;
            }

            Player player = dplayer.getPlayer();

            player.sendMessage(main.prefix + message);

            player.sendMessage(ChatColor.GRAY + "You have recieved " + ChatColor.DARK_RED +
                    dplayer.getGameScore() + " Bloodpoints");
            dplayer.setBloodPoints(dplayer.getBloodPoints() + dplayer.getGameScore());
            dplayer.addToStaticScore(dplayer.getGameScore());
            main.getSqlManager().uploadUserStats(dplayer);
            dplayer.clearGameScore();
        }
    }

    public void countDownBleep(int timeLeft) {
        for (DPlayer player : players) {
            Player actual = player.getPlayer();
            actual.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
                            "&7Summoned in " + timeLeft + "s")));
            actual.setLevel(timeLeft);
            actual.playSound(actual.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0F, 1.0F);
        }
    }

    public void gameStartBleep(int timeLeft) {
        for (DPlayer player : players) {
            Player actual = player.getPlayer();
            actual.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
                            "&7Game starts in " + timeLeft + "s")));
            actual.setLevel(timeLeft);

            actual.playSound(actual.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0F, 2.0F);
        }
    }

    public void startGameSound() {
        for (DPlayer player : players) {
            Player actual = player.getPlayer();
            actual.playSound(actual.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0F, 1.0F);
        }
    }

    public void endGameSound() {
        for (DPlayer player : players) {
            Player actual = player.getPlayer();
            actual.playSound(actual.getLocation(), Sound.ENTITY_WITHER_DEATH, 1.0F, 1.0F);
        }
    }
}
