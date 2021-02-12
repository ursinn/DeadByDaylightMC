package noahnok.dbdl.files.utils.pagenation;

import noahnok.dbdl.files.utils.pagenation.buttons.PageItem;
import noahnok.dbdl.files.utils.pagenation.interfaces.GeneralClick;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class PageEvent implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTopInventory().getHolder() instanceof Page) {
            e.setCancelled(true);

            if (e.getWhoClicked() instanceof Player) {
                Player player = (Player) e.getWhoClicked();

                ItemStack itemStack = e.getCurrentItem();

                if (itemStack == null || itemStack.getType() == Material.AIR) {
                    return;
                }

                Page page = (Page) e.getView().getTopInventory().getHolder();

                PageItem pageItem = page.getPageItem(e.getRawSlot());

                if (pageItem == null) {
                    return;
                }

                for (GeneralClick click : pageItem.getClickActions()) {
                    click.click(player);
                }
            }
        }
    }
}
