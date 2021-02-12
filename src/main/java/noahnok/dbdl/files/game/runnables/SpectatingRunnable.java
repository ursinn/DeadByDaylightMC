package noahnok.dbdl.files.game.runnables;

import noahnok.dbdl.files.player.DPlayer;
import org.bukkit.scheduler.BukkitRunnable;

public class SpectatingRunnable extends BukkitRunnable {

    private final DPlayer spectator;
    private String spectating;

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
