package noahnok.DBDL.files.game;

;
import noahnok.DBDL.files.DeadByDaylight;
import noahnok.DBDL.files.game.levers.DLever;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class ExitGate {
    private DeadByDaylight main;
    private List<Location> locs = new ArrayList<Location>();
    private String facing;

    private Location center;

    private DLever lever1;
    private DLever lever2;

    public DLever getLever1() {
        return lever1;
    }

    public void setLever1(DLever lever1) {
        this.lever1 = lever1;
    }

    public DLever getLever2() {
        return lever2;
    }

    public void setLever2(DLever lever2) {
        this.lever2 = lever2;
    }

    public ExitGate(String facing, Location center, DeadByDaylight main){
        this.facing = facing;
        this.center = center;
        this.main = main;

    }

    public void openGate(){
        int timeDelay = 0;
        for (final List<Location> locs : getRows()){
            new BukkitRunnable(){
                public void run() {
                    for (Location loc : locs){
                        loc.getBlock().getDrops().clear();
                        loc.getBlock().breakNaturally();

                        loc.getWorld().playSound(loc, Sound.BLOCK_ANVIL_BREAK, 1.0F, 1.0F);
                    }
                }
            }.runTaskLater(main, (20*timeDelay));
            timeDelay++;

        }
        new BukkitRunnable(){
            public void run() {
                Location loc = getCenter();
                loc.getWorld().playSound(loc, Sound.ENTITY_ENDERDRAGON_DEATH, 10F, 1.5F);

            }
        }.runTaskLater(main, 90);




    }

    private List<List<Location>> getRows(){
        int lowestY = 400;
        for (Location loc : this.locs){
            if (loc.getY() <= lowestY){
                lowestY = loc.getBlockY();
            }
        }

        List<List<Location>> rows = new ArrayList<List<Location>>();
        for (int i = 0; i < 3; i++){
            List<Location> row = new ArrayList<Location>();
            for (Location loc : this.locs){
                if (loc.getY() == (lowestY+i)){
                    row.add(loc);
                }
            }
            rows.add(row);
        }

        return rows;
    }

    public String getFacing() {
        return facing;
    }

    public List<Location> getLocs() {
        return locs;
    }

    public Location getCenter() {
        return center;
    }

    public void setLocs(List<Location> locs) {
        this.locs = locs;
    }
}
