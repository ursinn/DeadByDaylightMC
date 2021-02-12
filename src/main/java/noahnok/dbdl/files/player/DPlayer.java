package noahnok.dbdl.files.player;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import noahnok.dbdl.files.DeadByDaylight;
import noahnok.dbdl.files.game.DGame;
import noahnok.dbdl.files.game.player.states.PlayerState;
import noahnok.dbdl.files.game.runnables.SpectatingRunnable;
import noahnok.dbdl.files.utils.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DPlayer {

    private final DeadByDaylight main;

    private UUID id;
    private String name;
    private Config dataFile;
    private PlayerStatus status = PlayerStatus.OUT_OF_GAME;

    private PlayerState playerState;

    private boolean spectating;

    private DPlayer spectate;

    private BukkitTask spectatingRunnable;

    private int gameScore;

    private DGame currentGame;

    //Config values to be loaded
    private int bloodPoints;
    private int score;

    private int escapes;
    private int deaths;
    private int timesSacrificed;
    private int generatorsFixed;
    private int generatorsMessedup;
    private int timesHooked;
    private int hookEscapes;
    private int hookPulloff;
    private int gatesOpened;
    private int hooksBroken;
    private int heals;

    private int timesSacrificing;
    private int successfulSacrifices;
    private int kills;
    private int losses;
    private int survivorPickups;
    private int survivorPickupEscapes;
    private int playerEscapes;
    private int wins;

    public DPlayer(UUID id, Config dataFile, DeadByDaylight main) {
        this.id = id;
        this.dataFile = dataFile;
        this.name = Bukkit.getServer().getPlayer(id).getName();
        this.main = main;
    }

    public Map<String, Integer> returnGenericData() {
        Map<String, Integer> values = new HashMap<>();
        values.put("bloodPoints", bloodPoints);


        return values;
    }

    public DGame getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(DGame currentGame) {
        this.currentGame = currentGame;
    }

    public BukkitTask getSpectatingRunnable() {
        return spectatingRunnable;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public void resetPlayerState() {
        if (playerState != null) {
            main.getPlayerStateManager().survivorHealed(this, true);
            if (playerState.getBleedingRunnable() != null) {
                playerState.getBleedingRunnable().cancel();
                playerState.setBleedingRunnable(null);
            }
        }

        playerState = new PlayerState(this);
    }

    public void stopSpectating() {
        if (this.getSpectatingRunnable() != null) {
            this.getSpectatingRunnable().cancel();
            this.spectatingRunnable = null;
        }
    }

    public boolean isSpectating() {
        return spectating;
    }

    public void setSpectating(boolean spectating) {
        this.spectating = spectating;
    }

    public void startSpectating(DGame game) {
        Player player = this.getPlayer();
        player.setGameMode(GameMode.SPECTATOR);
        resetPlayerState();

        for (DPlayer dplayer : game.getHunted()) {
            if (dplayer != null && !dplayer.isDead() && !dplayer.isHunter()) {
                player.setSpectatorTarget(dplayer.getPlayer());
                this.setSpectating(true);
                spectate = dplayer;
                this.spectatingRunnable = new SpectatingRunnable(dplayer.getName(), this).runTaskTimer(main, 0, 80);
                return;
            }
        }

    }

    public void spectateNext(DPlayer player) {
        if (this.getPlayer().getGameMode() != GameMode.SPECTATOR) {
            this.getPlayer().setGameMode(GameMode.SPECTATOR);
        }

        if (!this.isSpectating()) {
            this.setSpectating(true);
        }

        this.getPlayer().setSpectatorTarget(player.getPlayer());
        spectate = player;
        spectatingRunnable.cancel();
        this.spectatingRunnable = new SpectatingRunnable(player.getName(), this).runTaskTimer(main, 0, 80);
    }

    public DPlayer getSpectate() {
        return spectate;
    }

    public boolean isPlayerASurvivorAndAlive() {
        return status.isAlive();
    }

    public boolean isHunter() {
        switch (this.status) {
            case HUNTER:
                return true;

            case CARRYING:
                return true;

            default:
                return false;
        }
    }

    public boolean isDead() {
        switch (this.status) {
            case DEAD:
                return true;

            case ESCAPED:
                return true;

            case SACRIFICED:
                return true;

            default:
                return false;
        }
    }

    public Player getPlayer() {
        return Bukkit.getServer().getPlayer(id);
    }

    public void addToScore(int amount) {
        gameScore += amount;
    }

    public int getGameScore() {
        return gameScore;
    }

    public void addToStaticScore(int value) {
        score += value;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void clearGameScore() {
        gameScore = 0;
    }

    public UUID getId() {
        return id;
    }

    public FileConfiguration getDataFile() {
        return dataFile.getConfig();
    }

    public Config grabConfig() {
        return dataFile;
    }

    public void kill() {
        id = null;
        name = null;
        dataFile = null;
    }

    public Map<String, Integer> returnHuntedData() {
        Map<String, Integer> values = new HashMap<>();
        values.put("escapes", escapes);
        values.put("deaths", deaths);
        values.put("timesSacrificed", timesSacrificed);
        values.put("generatorsFixed", generatorsFixed);
        values.put("generatorsMessedup", generatorsMessedup);
        values.put("timesHooked", timesHooked);
        values.put("hookEscapes", hookEscapes);
        values.put("hookPulloff", hookPulloff);
        values.put("gatesOpened", gatesOpened);
        values.put("hooksBroken", hooksBroken);
        values.put("heals", heals);
        return values;
    }

    public Map<String, Integer> returnHunterData() {
        Map<String, Integer> values = new HashMap<>();
        values.put("timesSacrificing", timesSacrificing);
        values.put("successfulSacrifices", successfulSacrifices);
        values.put("kills", kills);
        values.put("losses", losses);
        values.put("survivorPickups", survivorPickups);
        values.put("survivorPickupEscapes", survivorPickupEscapes);
        values.put("playerEscapes", playerEscapes);


        return values;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public String getName() {
        return name;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    public void setStatus(PlayerStatus status) {
        this.status = status;
    }

    public int getBloodPoints() {
        return bloodPoints;
    }

    public void setBloodPoints(int bloodPoints) {
        this.bloodPoints = bloodPoints;
    }

    public int getEscapes() {
        return escapes;
    }

    public void setEscapes(int escapes) {
        this.escapes = escapes;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getTimesSacrificed() {
        return timesSacrificed;
    }

    public void setTimesSacrificed(int timesSacrificed) {
        this.timesSacrificed = timesSacrificed;
    }

    public int getGeneratorsFixed() {
        return generatorsFixed;
    }

    public void setGeneratorsFixed(int generatorsFixed) {
        this.generatorsFixed = generatorsFixed;
    }

    public int getGeneratorsMessedup() {
        return generatorsMessedup;
    }

    public void setGeneratorsMessedup(int generatorsMessedup) {
        this.generatorsMessedup = generatorsMessedup;
    }

    public int getTimesHooked() {
        return timesHooked;
    }

    public void setTimesHooked(int timesHooked) {
        this.timesHooked = timesHooked;
    }

    public int getHookEscapes() {
        return hookEscapes;
    }

    public void setHookEscapes(int hookEscapes) {
        this.hookEscapes = hookEscapes;
    }

    public int getHookPulloff() {
        return hookPulloff;
    }

    public void setHookPulloff(int hookPulloff) {
        this.hookPulloff = hookPulloff;
    }

    public int getGatesOpened() {
        return gatesOpened;
    }

    public void setGatesOpened(int gatesOpened) {
        this.gatesOpened = gatesOpened;
    }

    public int getHooksBroken() {
        return hooksBroken;
    }

    public void setHooksBroken(int hooksBroken) {
        this.hooksBroken = hooksBroken;
    }

    public int getHeals() {
        return heals;
    }

    public void setHeals(int heals) {
        this.heals = heals;
    }

    public int getTimesSacrificing() {
        return timesSacrificing;
    }

    public void setTimesSacrificing(int timesSacrificing) {
        this.timesSacrificing = timesSacrificing;
    }

    public int getSuccessfulSacrifices() {
        return successfulSacrifices;
    }

    public void setSuccessfulSacrifices(int successfulSacrifices) {
        this.successfulSacrifices = successfulSacrifices;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getSurvivorPickups() {
        return survivorPickups;
    }

    public void setSurvivorPickups(int survivorPickups) {
        this.survivorPickups = survivorPickups;
    }

    public int getSurvivorPickupEscapes() {
        return survivorPickupEscapes;
    }

    public void setSurvivorPickupEscapes(int survivorPickupEscapes) {
        this.survivorPickupEscapes = survivorPickupEscapes;
    }

    public int getPlayerEscapes() {
        return playerEscapes;
    }

    public void setPlayerEscapes(int playerEscapes) {
        this.playerEscapes = playerEscapes;
    }

    //Messaging utils for player

    public void sendAB(String message) {
        this.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR,
                TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', message)));
    }
}
