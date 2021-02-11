package noahnok.DBDL.files.utils.Pagenation;

import org.bukkit.entity.Player;

public interface Openable {

    void openInventory(Player p);

    void openInventory(Player p, int index);
}
