package noahnok.DBDL.files.utils.builders;

import noahnok.DBDL.files.utils.CustomHolder;
import noahnok.DBDL.files.utils.Icon;
import org.bukkit.inventory.ItemStack;

public class InventoryBuilder {


    public CustomHolder createNew(String name, int size) {
        return new CustomHolder(size, name);
    }

    public Icon createIcon(ItemStack i, String name) {
        return new Icon(i, name);
    }
}
