package noahnok.dbdl.files.game.generators;

import noahnok.dbdl.files.DeadByDaylight;
import noahnok.dbdl.files.game.DGame;
import noahnok.dbdl.files.player.DPlayer;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class Generator {

    private final DeadByDaylight main;
    private final Location loc;
    private DGame game;
    private double percentDone;

    private boolean finished;

    public Generator(DGame game, Location loc, DeadByDaylight main) {
        this.game = game;
        this.loc = loc;
        this.main = main;
    }

    public void spawn() {
        loc.getBlock().setType(Material.FURNACE);
    }

    public void increment(DPlayer p) {
        percentDone = percentDone + 1;
        if (percentDone >= 100) {
            finished = true;
            spawnFireworks();
            game.incrementGens();

            p.setGeneratorsFixed(p.getGeneratorsFixed() + 1);
        }
        p.addToScore((int) (5 * game.getMultiplier()));
    }

    public void complete() {
        finished = true;
    }

    private void spawnFireworks() {
        Firework fw = (Firework) this.loc.getWorld().spawnEntity(loc.clone().add(0, 1, 0), EntityType.FIREWORK);
        final FireworkMeta fwm = fw.getFireworkMeta();

        fwm.addEffect(FireworkEffect.builder().flicker(true).trail(true)
                .with(FireworkEffect.Type.BALL_LARGE).withColor(Color.RED).withFade(Color.BLACK).build());
        fwm.setPower(0);
        fw.setCustomName("DBDL-FIREWORK");
        fw.setCustomNameVisible(false);

        fw.setFireworkMeta(fwm);
        fw.detonate();

        new BukkitRunnable() {
            int i = 3;

            public void run() {
                if (i == 0) {
                    cancel();
                }
                Firework fw2 = (Firework) loc.getWorld().spawnEntity(loc.clone().add(0, 1, 0), EntityType.FIREWORK);
                fw2.setCustomName("DBDL-FIREWORK");
                fw2.setCustomNameVisible(false);

                fwm.setPower(3);
                fw2.setFireworkMeta(fwm);

                i--;
            }
        }.runTaskTimer(main, 0, 20);

    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void increment(double value) {
        percentDone += value;
    }

    public void setGame(DGame game) {
        this.game = game;
    }

    public void despawn() {
        loc.getBlock().setType(Material.AIR);
    }

    public Block getBlock() {
        return loc.getBlock();
    }

    public double getPercentDone() {
        return percentDone;
    }
}
