package noahnok.dbdl.files.utils.builders;

import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class FireworkBuilder {

    private final Firework fw;
    private final FireworkMeta fwm;

    public FireworkBuilder(Location loc) {
        fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        fwm = fw.getFireworkMeta();
    }

    public FireworkBuilder setPower(int power) {
        fwm.setPower(power);
        return this;
    }

    public FireworkBuilder addEffect(FireworkEffect e) {
        fwm.addEffect(e);
        return this;
    }

    public void build() {
        fw.setFireworkMeta(fwm);
        fw.detonate();
    }
}
