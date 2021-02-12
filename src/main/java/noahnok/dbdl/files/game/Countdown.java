package noahnok.dbdl.files.game;

import noahnok.dbdl.files.DeadByDaylight;
import noahnok.dbdl.files.signs.DSign;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Countdown {

    protected DeadByDaylight main;

    protected BukkitTask task;

    protected int timeInSeconds;
    protected int interval;
    protected int tempTime;
    protected DGame ref;
    boolean contAlert;

    public Countdown(int timeInSeconds, boolean contAlert, int interval, DGame ref, DeadByDaylight main) {
        this.timeInSeconds = timeInSeconds;
        this.contAlert = contAlert;
        this.interval = interval;
        this.ref = ref;
        this.main = main;
    }

    public void start() {
        tempTime = timeInSeconds;
        this.task = new BukkitRunnable() {
            public void run() {
                if (tempTime < 11 && tempTime > 0) {
                    ref.countDownBleep(tempTime);
                }

                if (tempTime <= 0) {

                    endCountdown(true);
                }
                tempTime--;
            }
        }.runTaskTimer(main, 0, 20);
    }

    public void endCountdown(boolean toPlay) {
        DSign sign = main.getSignManager().getSign(ref);
        if (sign != null) {
            sign.removeGame();
        }
        task.cancel();

        if (toPlay) {
            ref.announce("Leaving to arena");
            final InGameCountdown iGCD = new InGameCountdown(ref.getGamemode().getGameTime(), true, 30, ref, main);
            ref.setIgCD(iGCD);

            ref.teleportPlayers();
            ref.disallowAllMove();

            new BukkitRunnable() {
                int count = 5;

                public void run() {
                    ref.gameStartBleep(count);
                    if (count <= 0) {
                        ref.startGameSound();
                        cancel();
                    }

                    count--;
                }
            }.runTaskTimer(main, 0, 20);

            new BukkitRunnable() {
                public void run() {
                    ref.setStatus(STATUS.INGAME);
                    iGCD.start();
                    ref.allowAllMove();
                    ref = null;
                }
            }.runTaskLater(main, 20 * 5);
        }
        task = null;
    }

    public void cancel() {
        task.cancel();
        ref.announce("Countdown canceled");
    }
}
