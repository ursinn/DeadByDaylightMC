package noahnok.dbdl.files.utils.builders;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemBuilder {

    private final ItemStack item;


    public ItemBuilder(Material m) {
        this.item = new ItemStack(m, 1);
    }

    public ItemStack build() {
        return this.item;
    }

    public ItemBuilder setByte(short by) {
        item.setDurability(by);
        return this;
    }
}
