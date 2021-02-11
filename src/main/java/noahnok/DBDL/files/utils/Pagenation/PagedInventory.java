package noahnok.DBDL.files.utils.Pagenation;

import noahnok.DBDL.files.utils.Pagenation.buttons.PageItem;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class PagedInventory {


    private final List<Page> pages = new ArrayList<>();
    private final String id;
    private PageItem swapItem;


    public PagedInventory(String id) {
        this.id = id;
    }

    public List<Page> getPages() {
        return pages;
    }

    public Page getPage(int index) {
        return pages.get(index);
    }

    public boolean addPage(Page page) {
        page.setSwapItem(swapItem);
        return pages.add(page);
    }

    public void addPage(Page page, int index) {
        pages.add(index, page);
    }

    public boolean removePage(Page page) {
        return pages.remove(page);
    }

    public void removePage(int index) {
        pages.remove(index);
    }

    public boolean addPages(List<Page> pages) {
        return pages.addAll(pages);
    }

    public boolean removePages(List<Page> pages) {
        return pages.removeAll(pages);
    }

    public int getPageIndex(Page page) {
        return pages.indexOf(page);
    }


    public Inventory showPage(int index) {
        Page page = pages.get(index);

        return page.getInventory();


    }

    public boolean hasNextPage(Page page) {
        return (pages.indexOf(page) + 1) != pages.size();
    }

    public boolean hasPreviousPage(Page page) {
        return pages.indexOf(page) != 0;
    }


    public void setSwapItem(PageItem swapItem) {
        this.swapItem = swapItem;
    }


}
