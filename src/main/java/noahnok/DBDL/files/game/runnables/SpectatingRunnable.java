package noahnok.DBDL.files.game.runnables;

import noahnok.DBDL.files.player.DPlayer;
import org.bukkit.scheduler.BukkitRunnable;

public class SpectatingRunnable extends BukkitRunnable {

    private String spectating;
    private DPlayer spectator;


    public SpectatingRunnable(String spectating, DPlayer spectator) {
        this.spectating = spectating;
        this.spectator = spectator;
    }

    @Override
    public void run() {
        spectator.sendAB("&7You are spectating &6&l" + spectating);


    }


    public void setSpectating(String spectating) {
        this.spectating = spectating;
    }


}
