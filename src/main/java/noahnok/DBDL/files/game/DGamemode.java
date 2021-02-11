package noahnok.DBDL.files.game;

import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class DGamemode {
    private String ID;

    private int hunters;
    private int hunted;
    private int maxgenerators;
    private int maxchests;
    private int maxhooks;


    private int gameTime; //In seconds
    private boolean allowPerks;
    private boolean allowItems;
    private boolean allowSacrifices;
    private boolean allowBleeding;
    private boolean allowStage3;
    private boolean allowStage2;
    private boolean instantSacrifice;
    private boolean useTrapdoor;

    private Set<ItemStack> disallowedItems = new HashSet<ItemStack>();
    private Set<String> disallowedPerks = new HashSet<String>();
    private Set<String> disallowedSacrifices = new HashSet<String>();


    public DGamemode(String ID, int hunters, int hunted, int maxgenerators, int maxchests, int maxhooks, int gameTime, boolean allowPerks, boolean allowItems, boolean allowSacrifices, boolean allowBleeding, boolean allowStage3, boolean allowStage2, boolean instantSacrifice, boolean useTrapdoor, Set<ItemStack> disallowedItems, Set<String> disallowedPerks, Set<String> disallowedSacrifices) {
        this.ID = ID;
        this.hunters = hunters;
        this.hunted = hunted;
        this.maxgenerators = maxgenerators;
        this.maxchests = maxchests;
        this.maxhooks = maxhooks;
        this.gameTime = gameTime;
        this.allowPerks = allowPerks;
        this.allowItems = allowItems;
        this.allowSacrifices = allowSacrifices;
        this.allowBleeding = allowBleeding;
        this.allowStage3 = allowStage3;
        this.allowStage2 = allowStage2;
        this.instantSacrifice = instantSacrifice;
        this.useTrapdoor = useTrapdoor;
        this.disallowedItems = disallowedItems;
        this.disallowedPerks = disallowedPerks;
        this.disallowedSacrifices = disallowedSacrifices;
    }

    public DGamemode(String ID, int hunters, int hunted, int maxgenerators, int maxchests, int maxhooks, int gameTime, boolean allowPerks, boolean allowItems, boolean allowSacrifices, boolean allowBleeding, boolean allowStage3, boolean allowStage2, boolean instantSacrifice, boolean useTrapdoor) {
        this.ID = ID;
        this.hunters = hunters;
        this.hunted = hunted;
        this.maxgenerators = maxgenerators;
        this.maxchests = maxchests;
        this.maxhooks = maxhooks;
        this.gameTime = gameTime;
        this.allowPerks = allowPerks;
        this.allowItems = allowItems;
        this.allowSacrifices = allowSacrifices;
        this.allowBleeding = allowBleeding;
        this.allowStage3 = allowStage3;
        this.allowStage2 = allowStage2;
        this.instantSacrifice = instantSacrifice;
        this.useTrapdoor = useTrapdoor;

    }

    public DGamemode(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public int getHunters() {
        return hunters;
    }

    public int getHunted() {
        return hunted;
    }

    public int getMaxgenerators() {
        return maxgenerators;
    }

    public int getMaxchests() {
        return maxchests;
    }

    public int getMaxhooks() {
        return maxhooks;
    }

    public int getGameTime() {
        return gameTime;
    }

    public boolean isAllowPerks() {
        return allowPerks;
    }

    public boolean isAllowItems() {
        return allowItems;
    }

    public boolean isAllowSacrifices() {
        return allowSacrifices;
    }

    public boolean isAllowBleeding() {
        return allowBleeding;
    }

    public boolean isAllowStage3() {
        return allowStage3;
    }

    public boolean isAllowStage2() {
        return allowStage2;
    }

    public boolean isInstantSacrifice() {
        return instantSacrifice;
    }

    public boolean isUseTrapdoor() {
        return useTrapdoor;
    }

    public Set<ItemStack> getDisallowedItems() {
        return disallowedItems;
    }

    public Set<String> getDisallowedPerks() {
        return disallowedPerks;
    }

    public Set<String> getDisallowedSacrifices() {
        return disallowedSacrifices;
    }
}
