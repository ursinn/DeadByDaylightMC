package noahnok.dbdl.files.game;

import noahnok.dbdl.files.DeadByDaylight;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class DGamemodeManager {

    private final DeadByDaylight main;
    private final Set<DGamemode> gamemodes = new HashSet<DGamemode>();

    public DGamemodeManager(DeadByDaylight main) {
        this.main = main;
    }

    public Set<DGamemode> getGamemodes() {
        return this.gamemodes;
    }

    public DGamemode getMode(String mode) {
        for (DGamemode gmode : gamemodes) {
            if (gmode.getID().equalsIgnoreCase(mode)) {
                return gmode;
            }
        }
        return null;
    }

    public boolean createGamemode(String mode) {
        DGamemode gmode = new DGamemode(mode);
        boolean value = gamemodes.add(gmode);
        Bukkit.getLogger().info(gamemodes.size() + "");
        return value;
    }

    public void addGamemode(DGamemode mode) {
        gamemodes.add(mode);
    }

    public DGamemode getGamemodeFromString(String mode) {
        for (DGamemode gamemode : gamemodes) {
            if (gamemode.getID().equals(mode)) {
                return gamemode;
            }
        }
        return null;
    }

    public void loadGamemodesFromFile() {
        int count = 0;

        for (String key : main.getGamemodesConfig().getConfig().getConfigurationSection("gamemodes").getKeys(false)) {
            String path = "gamemodes." + key + ".";
            int hunters, hunted, generators, chests, hooks = 0;
            boolean perks, items, offerings, bleeding, stage2, stage3, trapdoor = true;
            boolean instantSacrifice = false;
            try {
                hunters = (Integer) getItem(path + "hunters");
                hunted = (Integer) getItem(path + "hunted");
                generators = (Integer) getItem(path + "max.generators");
                chests = (Integer) getItem(path + "max.chests");
                hooks = (Integer) getItem(path + "max.hooks");

                perks = (Boolean) getItem(path + "allow.perks");
                items = (Boolean) getItem(path + "allow.items");
                offerings = (Boolean) getItem(path + "allow.offerings");
                bleeding = (Boolean) getItem(path + "allow.bleeding");
                stage2 = (Boolean) getItem(path + "allow.stage2");
                stage3 = (Boolean) getItem(path + "allow.stage3");
                instantSacrifice = (Boolean) getItem(path + "allow.instantSacrifice");
                trapdoor = (Boolean) getItem(path + "allow.trapdoor");

            } catch (NullPointerException e) {
                main.getLogger().severe("Hmm... seems the gamemode: " + key + " failed to load! Something wasn't set or is broken! Please check you gamemodes.yml");
                continue;
            }

            Set<String> disPerks = (Set<String>) getItem(path + "disallow.perks");
            Set<ItemStack> disItems = (Set<ItemStack>) getItem(path + "disallow.items");
            Set<String> disOfferings = (Set<String>) getItem(path + "disallow.offerings");

            DGamemode newmode = new DGamemode(key, hunters, hunted, generators, chests, hooks, 3600, perks, items, offerings, bleeding, stage3, stage2, instantSacrifice, trapdoor, disItems, disPerks, disOfferings);

            main.getGamemodeManager().addGamemode(newmode);

            count++;
        }
        main.getLogger().info("Found " + count + " custom gamemode(s)!");
    }

    private Object getItem(String path) {
        return main.getGamemodesConfig().getConfig().get(path);
    }
}
