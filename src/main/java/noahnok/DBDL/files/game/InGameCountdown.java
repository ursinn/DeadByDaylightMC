package noahnok.DBDL.files.game;

import noahnok.DBDL.files.DeadByDaylight;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

;

public class InGameCountdown {


    protected DeadByDaylight main;

    protected BukkitTask task;

    protected int timeInSeconds;
    protected int interval;
    protected int tempTime;

    boolean contAlert;

    protected DGame ref;



    public InGameCountdown(int timeInSeconds, boolean contAlert, int interval, DGame ref, DeadByDaylight main){
        this.timeInSeconds = timeInSeconds;
        this.contAlert = contAlert;
        this.interval = interval;
        this.ref = ref;
        this.main = main;

    }

    public void start(){
        tempTime = timeInSeconds;
        this.task = new BukkitRunnable(){
            public void run() {
                if (tempTime % interval == 0 && !(tempTime < interval) && tempTime >= 11 && tempTime != timeInSeconds){
                    if (contAlert) {
                        ref.announce("Game ends in " + getTimeAsString(tempTime));
                    }
                }
                if (tempTime < 11){
                    ref.announce("Game ends in " + tempTime + "s");

                }

                if (tempTime <= 0){
                    endCountdown();
                }
                tempTime--;

            }
        }.runTaskTimer(main, 0, 20);
    }

    public void endCountdown(){

        task.cancel();

        main.getGameManager().endGame(ref);
        ref = null;
        task = null;



    }

    private String getTimeAsString(int time){
        return (Math.floorDiv(time, 60) + "m" + (time % 60) + "s") + " ";
    }

    public void cancel(){
        task.cancel();
        ref.announce("Countdown canceled");
    }
}
