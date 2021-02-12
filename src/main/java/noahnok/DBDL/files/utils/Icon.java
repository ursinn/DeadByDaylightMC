package noahnok.dbdl.files.utils;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Icon extends ItemStack {
    public final ItemStack itemStack;

    public final List<ClickAction> clickActions = new ArrayList<ClickAction>();

    public Icon(ItemStack itemStack, String name) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        itemStack.setItemMeta(meta);
        this.itemStack = itemStack;
    }

    private Icon(Icon ico) {
        this.itemStack = ico.getItemStack();
    }

    public Icon addClickAction(ClickAction clickAction) {
        this.clickActions.add(clickAction);
        return this;
    }

    public List<ClickAction> getClickActions() {
        return this.clickActions;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public Icon addLore(String lore) {
        ItemMeta meta = itemStack.getItemMeta();
        List<String> lorelist = meta.getLore();
        if (lorelist == null) {
            lorelist = new ArrayList<String>();
        }

        lorelist.add(ChatColor.translateAlternateColorCodes('&', lore));
        meta.setLore(lorelist);
        itemStack.setItemMeta(meta);
        return this;
    }

    public Icon getCopy() {
        return new Icon(this);
    }

    public Icon clearLore() {
        ItemMeta meta = itemStack.getItemMeta();
        meta.setLore(null);
        itemStack.setItemMeta(meta);
        return this;
    }
}
