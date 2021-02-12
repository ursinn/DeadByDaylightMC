package noahnok.dbdl.files.utils.pagenation;

import org.bukkit.entity.Player;

public interface Openable {

    void openInventory(Player p);

    void openInventory(Player p, int index);
}
