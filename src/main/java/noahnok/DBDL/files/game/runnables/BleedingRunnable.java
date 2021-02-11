package noahnok.DBDL.files.game.runnables;

import noahnok.DBDL.files.DeadByDaylight;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.ThreadLocalRandom;

public class BleedingRunnable extends BukkitRunnable {

    private Player player;
    private DeadByDaylight main;

    public BleedingRunnable(Player player, DeadByDaylight main) {
        this.player = player;
        this.main = main;
    }

    @Override
    public void run() {
        //Red particle
        new BukkitRunnable() {

            int count = 0;
            private Location current = player.getLocation().clone();

            private Location randomOffset(Location loc) {
                int rand = ThreadLocalRandom.current().nextInt(0, 1);
                Location newloc = loc.clone().add(rand, 0.2, rand);
                return newloc;
            }

            @Override
            public void run() {
                if (count < 50) {
                    Location thisIt = randomOffset(current);

                    for (int i = 0; i < 4; i++) {
                        thisIt.getWorld().spawnParticle(Particle.REDSTONE, thisIt, 0, 1.0, 0.0, 0.0, 1.0);
                    }
                    count++;
                } else {

                    cancel();
                }
            }
        }.runTaskTimer(main, 0, 4);


    }
}
