package noahnok.dbdl.files.game.playerStates;

import noahnok.dbdl.files.player.DPlayer;
import org.bukkit.scheduler.BukkitTask;

public class PlayerState {

    private final DPlayer player;
    // Ingame playable states
    private boolean bleeding, crawling, injured, hooked, carried, beingHealed;
    // Only applicable for the HUNTER/KILLER
    private boolean carrying, stunned, blind, fatigued;
    // Game end states
    private EndGameStates endGameState;
    private HookedStages hookedStage;
    private BukkitTask bleedingRunnable;

    public PlayerState(DPlayer player) {
        this.player = player;
        bleeding = false;
        crawling = false;
        injured = false;
        hooked = false;
        carried = false;
        endGameState = EndGameStates.NONE;
        hookedStage = HookedStages.NOT_HOOKED;
    }

    public BukkitTask getBleedingRunnable() {
        return bleedingRunnable;
    }

    public void setBleedingRunnable(BukkitTask bleedingRunnable) {
        this.bleedingRunnable = bleedingRunnable;
    }

    public boolean isBleeding() {
        return bleeding;
    }

    public void setBleeding(boolean bleeding) {
        this.bleeding = bleeding;
    }

    public boolean isBeingHealed() {
        return beingHealed;
    }

    public void setBeingHealed(boolean beingHealed) {
        this.beingHealed = beingHealed;
    }

    public boolean isCarrying() {
        return carrying;
    }

    public void setCarrying(boolean carrying) {
        this.carrying = carrying;
    }

    public boolean isStunned() {
        return stunned;
    }

    public void setStunned(boolean stunned) {
        this.stunned = stunned;
    }

    public boolean isBlind() {
        return blind;
    }

    public void setBlind(boolean blind) {
        this.blind = blind;
    }

    public boolean isFatigued() {
        return fatigued;
    }

    public void setFatigued(boolean fatigued) {
        this.fatigued = fatigued;
    }

    public HookedStages getHookedStage() {
        return hookedStage;
    }

    public void setHookedStage(HookedStages hookedStage) {
        this.hookedStage = hookedStage;
    }

    public boolean isCrawling() {
        return crawling;
    }

    public void setCrawling(boolean crawling) {
        this.crawling = crawling;
    }

    public boolean isInjured() {
        return injured;
    }

    public void setInjured(boolean injured) {
        this.injured = injured;
    }

    public boolean isHooked() {
        return hooked;
    }

    public void setHooked(boolean hooked) {
        this.hooked = hooked;
    }

    public boolean isCarried() {
        return carried;
    }

    public void setCarried(boolean carried) {
        this.carried = carried;
    }

    public EndGameStates getEndGameState() {
        return endGameState;
    }

    public void setEndGameState(EndGameStates endGameState) {
        this.endGameState = endGameState;
    }

    public DPlayer getPlayer() {
        return player;
    }
}
