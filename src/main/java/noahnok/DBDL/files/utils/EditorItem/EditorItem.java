package noahnok.DBDL.files.utils.EditorItem;

import org.bukkit.ChatColor;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class EditorItem extends ItemStack{
    public ItemStack item;

    public final List<ItemExecutor> executors = new ArrayList<ItemExecutor>();
    public final List<ItemDeExecutor> deExecutors = new ArrayList<ItemDeExecutor>();

    public EditorItem(ItemStack i, String name){
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        List<String> lore = meta.getLore();
        if (lore == null){
            lore = new ArrayList<String>();
        }
        lore.add(ChatColor.COLOR_CHAR+ "§D§B§D§L§-§E§D§I§T§-§I§T§E§M");
        meta.setLore(lore);
        i.setItemMeta(meta);
        this.item = i;
    }

    public EditorItem addExecutor(ItemExecutor exe){
        this.executors.add(exe);
        return this;
    }

    public List<ItemExecutor> getExecutors() {
        return executors;
    }

    public List<ItemDeExecutor> getDeExecutors() {
        return deExecutors;
    }

    public EditorItem addDeExecutor(ItemDeExecutor exe){
        this.deExecutors.add(exe);
        return this;
    }

    public ItemStack getItem() {
        return item;
    }

}
