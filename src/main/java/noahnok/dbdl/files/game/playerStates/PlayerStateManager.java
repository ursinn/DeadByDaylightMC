package noahnok.dbdl.files.game.playerStates;

import noahnok.dbdl.files.DeadByDaylight;
import noahnok.dbdl.files.game.runnables.BleedingRunnable;
import noahnok.dbdl.files.player.DPlayer;
import noahnok.dbdl.files.player.PlayerStatus;

public class PlayerStateManager {

    private final DeadByDaylight main;

    public PlayerStateManager(DeadByDaylight main) {
        this.main = main;
    }

    public void survivorHit(DPlayer survivor) {
        if (survivor.getPlayerState().isCrawling()) {
            survivor.setStatus(PlayerStatus.DEAD);
            survivor.startSpectating(survivor.getCurrentGame());
            main.getGameManager().canGameEnd(survivor.getCurrentGame());
            return;
        }
        if (!survivor.getPlayerState().isInjured()) {
            survivorHitToInjured(survivor);
        } else {
            survivorHitToCrawling(survivor);
        }
    }

    private void survivorHitToInjured(DPlayer survivor) {

        setBleeding(survivor);
        survivor.getPlayerState().setInjured(true);
        injureSpeed(survivor);
    }

    private void survivorHitToCrawling(DPlayer survivor) {
        setBleeding(survivor);
        survivor.getPlayerState().setInjured(false);
        survivor.getPlayerState().setCrawling(true);

        survivor.getPlayer().setSneaking(true);

        crawlSpeed(survivor);
    }

    public void survivorPickedUpByHunter(DPlayer survivor) {
        survivor.getPlayerState().setCrawling(false);
        survivor.getPlayerState().setInjured(false);
        survivor.getPlayerState().setCarried(true);

        noSpeed(survivor);
    }

    // atWill mean the survivor escaped them selves and wasn't dropped by the killer purposely
    public void survivorEscapeHunterPickup(DPlayer survivor, boolean atWill) {
        survivor.getPlayerState().setCarried(false);
        if (atWill) {
            survivorHitToInjured(survivor);
        } else {
            survivorHitToCrawling(survivor);
        }
    }

    public void survivorHooked(DPlayer survivor) {
        survivor.getPlayerState().setHooked(true);
        survivor.getPlayerState().setCarried(false);

        stopBleeding(survivor);

        switch (survivor.getPlayerState().getHookedStage()) {
            case HookedStages.NOT_HOOKED:
                survivor.getPlayerState().setHookedStage(HookedStages.STAGE_1);
                break;

            case HookedStages.STAGE_1:
                survivor.getPlayerState().setHookedStage(HookedStages.STAGE_2);
                break;

            case HookedStages.STAGE_2:
                // Survivor has been taken by the Entity
                survivor.getPlayerState().setHookedStage(HookedStages.DEAD);
                survivor.getPlayerState().setEndGameState(EndGameStates.SACRIFICED);
                survivor.getPlayerState().setBleeding(false);

                break;
        }
    }

    public void survivorUnhooked(DPlayer survivor) {
        survivor.getPlayerState().setHooked(false);
        survivor.getPlayerState().setInjured(true);

        setBleeding(survivor);
    }

    public void survivorHealingStart(DPlayer survivor) {
        survivor.getPlayerState().setBeingHealed(true);
    }

    public void survivorHealingStop(DPlayer survivor) {
        survivor.getPlayerState().setBeingHealed(false);
    }

    public void survivorHealed(DPlayer survivor, boolean fullHealth) {
        if (fullHealth) {
            stopBleeding(survivor);
            survivor.getPlayerState().setInjured(false);
            normalSpeed(survivor);
        } else {
            survivor.getPlayerState().setCrawling(false);
            survivor.getPlayerState().setInjured(true);
            injureSpeed(survivor);
        }

    }

    public void survivorKilledByHuntersHand(DPlayer survivor) {
        survivor.getPlayerState().setCrawling(false);
        stopBleeding(survivor);
        survivor.getPlayerState().setEndGameState(EndGameStates.DEAD);
    }

    public void survivorEscapes(DPlayer survivor) {
        survivor.getPlayerState().setEndGameState(EndGameStates.ESCAPED);
        stopBleeding(survivor);

    }

    public boolean isSurvivorDead(DPlayer survivor) {
        return survivor.getPlayerState().getEndGameState() == EndGameStates.DEAD;
    }

    public boolean isSurvivorHooked(DPlayer survivor) {
        return survivor.getPlayerState().isHooked();
    }

    public boolean hasSurvivorEscape(DPlayer survivor) {
        return survivor.getPlayerState().getEndGameState() == EndGameStates.ESCAPED;
    }

    public boolean isSurvivorSacrificed(DPlayer survivor) {
        return survivor.getPlayerState().getEndGameState() == EndGameStates.SACRIFICED;
    }

    // The following are only available to the HUNTER/KILLER
    public void hunterStartCarrying(DPlayer hunter) {
        hunter.getPlayerState().setCarrying(true);
    }

    // atWill means if the Hunter decided to put the survivor down themselves or if they escaped
    public void hunterStopCarrying(DPlayer hunter, boolean atWill) {
        hunter.getPlayerState().setCarrying(false);

        if (!atWill) {
            hunterStunned(hunter);
        }
    }

    public void hunterStunned(DPlayer hunter) {
        hunter.getPlayerState().setStunned(true);
    }

    public void hunterUnStunned(DPlayer hunter) {
        hunter.getPlayerState().setStunned(false);
    }

    public void hunterBlinded(DPlayer hunter) {
        hunter.getPlayerState().setBlind(true);
    }

    public void hunterUnBlinded(DPlayer hunter) {
        hunter.getPlayerState().setBlind(false);
    }

    public void hunterFatigued(DPlayer hunter) {
        hunter.getPlayerState().setFatigued(true);
    }

    public void hunterUnFatigued(DPlayer hunter) {
        hunter.getPlayerState().setFatigued(false);
    }

    private void crawlSpeed(DPlayer survivor) {
        survivor.getPlayer().setWalkSpeed((float) 0.1);
    }

    private void injureSpeed(DPlayer survivor) {
        survivor.getPlayer().setWalkSpeed((float) 0.15);
    }

    private void normalSpeed(DPlayer survivor) {
        survivor.getPlayer().setWalkSpeed((float) 0.2);
    }

    private void noSpeed(DPlayer survivor) {
        survivor.getPlayer().setWalkSpeed(0);
    }

    private void setBleeding(DPlayer player) {
        if (player.getPlayerState().getBleedingRunnable() == null) {
            player.getPlayerState().setBleedingRunnable(
                    new BleedingRunnable(player.getPlayer(), main).runTaskTimer(main, 0, 30));
        }
    }

    private void stopBleeding(DPlayer player) {
        if (player.getPlayerState().getBleedingRunnable() != null) {
            player.getPlayerState().getBleedingRunnable().cancel();
            player.getPlayerState().setBleedingRunnable(null);
        }
    }
}
