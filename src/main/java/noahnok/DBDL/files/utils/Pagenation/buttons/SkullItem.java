package noahnok.DBDL.files.utils.Pagenation.buttons;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

public class SkullItem extends PageItem {


    public SkullItem() {
        super.setMaterial(Material.SKULL_ITEM);
    }


    public SkullItem setSkull(UUID id) {
        ItemStack item = super.getItem();
        item.setItemMeta(super.meta);
        item.setDurability((short) 3);
        SkullMeta smeta = (SkullMeta) item.getItemMeta();
        smeta.setOwningPlayer(Bukkit.getOfflinePlayer(id));
        item.setItemMeta(smeta);
        super.item = item;

        return this;
    }

    @Override
    public ItemStack getItem() {
        return super.item;
    }
}
