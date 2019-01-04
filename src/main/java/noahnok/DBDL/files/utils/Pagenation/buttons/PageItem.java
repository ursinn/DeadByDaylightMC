package noahnok.DBDL.files.utils.Pagenation.buttons;

import noahnok.DBDL.files.utils.Pagenation.PageUtils;
import noahnok.DBDL.files.utils.Pagenation.interfaces.GeneralClick;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PageItem {

    protected ItemStack item;
    protected ItemMeta meta;

    private Collection<GeneralClick> clickActions = new ArrayList<>();


    public PageItem(){
        this.item = new ItemStack(Material.BARRIER, 1);
        this.meta = this.item.getItemMeta();

        List<String> lore = new ArrayList<>();
        this.meta.setLore(lore);
    }

    public PageItem(Material material, int amount) {
        this.item = new ItemStack(material, amount);

        this.meta = this.item.getItemMeta();

        List<String> lore = new ArrayList<>();
        this.meta.setLore(lore);
    }

    public PageItem setMaterial(Material m){
        this.item.setType(m);
        return this;
    }

    public PageItem(ItemStack item){
        this.item = item;
        this.meta = item .getItemMeta();

        List<String> lore = new ArrayList<>();
        this.meta.setLore(lore);
    }

    public PageItem setDisplayName(String name){
        meta.setDisplayName(PageUtils.color(name));
        return this;
    }

    public String getDisplayName(){
        return meta.getDisplayName();
    }

    public PageItem addLore(String loreline){
        List<String> lore = new ArrayList<>(meta.getLore());
        lore.add(PageUtils.color(loreline));
        meta.setLore(lore);
        return this;
    }

    public PageItem setLore(String loreline, int index){
        List<String> lore = new ArrayList<>(meta.getLore());
        lore.set(index, PageUtils.color(loreline));
        meta.setLore(lore);
        return this;
    }

    public PageItem removeLore(int index){
        List<String> lore = new ArrayList<>(meta.getLore());
        lore.remove(index);
        meta.setLore(lore);
        return this;
    }

    public ItemStack getItem(){
        this.item.setItemMeta(this.meta);
        return this.item;
    }

    public PageItem addClickAction(GeneralClick click){
        clickActions.add(click);
        return this;

    }

    public PageItem copy(){
        this.item.setItemMeta(this.meta);
        return new PageItem(this.item);

    }



    public Collection<GeneralClick> getClickActions() {
        return clickActions;
    }
}
