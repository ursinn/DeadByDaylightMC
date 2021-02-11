package noahnok.DBDL.files.game.runnables;

import noahnok.DBDL.files.DeadByDaylight;
import noahnok.DBDL.files.game.DGame;
import noahnok.DBDL.files.player.DPlayer;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class NoJump extends BukkitRunnable {

    private final DeadByDaylight main;


    public NoJump(DeadByDaylight main) {
        this.main = main;

    }

    @Override
    public void run() {
        for (DGame game : main.getGameManager().getGames()) {
            for (DPlayer player : game.getPlayers()) {
                player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, (20 * 8), 250));
            }
        }
    }
}
