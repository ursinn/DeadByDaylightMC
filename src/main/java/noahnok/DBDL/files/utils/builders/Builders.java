package noahnok.DBDL.files.utils.builders;

import org.bukkit.Material;

public class Builders {


    public ItemBuilder getNewBuilder(Material m) {
        return new ItemBuilder(m);
    }
}
