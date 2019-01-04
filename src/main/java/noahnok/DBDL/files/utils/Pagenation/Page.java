package noahnok.DBDL.files.utils.Pagenation;

import noahnok.DBDL.files.utils.Pagenation.buttons.PageItem;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.Map;

public class Page implements InventoryHolder {

    Map<Integer, PageItem> pageItems = new HashMap<>();

    private String pageName;

    private PageItem swapItemLeft,swapItemRight;

    private PagedInventory pagedInventory;

    private int pageSize = -1;


    public void setSwapItem(PageItem swapItem) {
        this.swapItemLeft = swapItem.copy().setDisplayName("&6Previous Page").addClickAction(p -> p.openInventory(pagedInventory.showPage(pagedInventory.getPageIndex(this) - 1)));
        this.swapItemRight = swapItem.copy().setDisplayName("&6Next Page").addClickAction(p -> p.openInventory(pagedInventory.showPage(pagedInventory.getPageIndex(this) + 1)));
    }

    public Page(String pageName, PagedInventory inv) {
        this.pageName = PageUtils.color(pageName);
        this.pagedInventory = inv;
    }


    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void addPageItem(PageItem item, int index){
        pageItems.put(index, item);
    }

    public boolean removePageItem(PageItem item){
        return pageItems.values().remove(item);
    }

    public void removePageItem(int index){
        pageItems.remove(index);
    }

    public PageItem getPageItem(int index){
        return pageItems.get(index);
    }

    public void setPageItem(PageItem item, int index){
        pageItems.put(index, item);
    }

    public Map<Integer,PageItem> getPageItems() {
        return pageItems;
    }

    private Integer invSize(){
        int largestSize = 0;
        for (int size : pageItems.keySet()){
            if (size > largestSize && pageItems.get(size) != swapItemRight && pageItems.get(size) != swapItemLeft){
                largestSize = size;
            }
        }
        int timesNineGoesInWhole = Math.floorDiv(largestSize, 9)+1;
        if (largestSize % 9 != 0){
            return (timesNineGoesInWhole*9)+9;
        }else{
            return (timesNineGoesInWhole*9);
        }
    }

    @Override
    public Inventory getInventory() {
        Inventory inv;

        if (pageSize == -1) {
            int invSize = invSize();


            if (pagedInventory != null && pagedInventory.hasNextPage(this) == false && pagedInventory.hasPreviousPage(this) == false) {
                invSize -= 9;
            }

            inv = Bukkit.createInventory(this, invSize, pageName);

            if (pagedInventory != null && swapItemLeft != null && pagedInventory.hasPreviousPage(this)) {
                pageItems.put((invSize - 8 - 1), swapItemLeft);
            }

            if (pagedInventory != null && swapItemRight != null && pagedInventory.hasNextPage(this)) {
                pageItems.put(invSize - 1, swapItemRight);
            }


        }else{
            inv = Bukkit.createInventory(this, pageSize*9, pageName);
        }

        for (Map.Entry<Integer, PageItem> entry : this.pageItems.entrySet()) {
            inv.setItem(entry.getKey(), entry.getValue().getItem());
        }





        return inv;
    }
}
