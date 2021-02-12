package noahnok.dbdl.files.utils;

import noahnok.dbdl.files.DeadByDaylight;
import noahnok.dbdl.files.game.DArena;
import noahnok.dbdl.files.game.DGamemode;
import noahnok.dbdl.files.utils.builders.Builders;
import noahnok.dbdl.files.utils.builders.InventoryBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class ArenaManagmentInvs {

    private final DeadByDaylight main;
    private final InventoryBuilder invBuilder = new InventoryBuilder();
    private final Builders itemBuilder = new Builders();
    private Icon listArenas;
    private Icon closeMenu;
    private Icon backMenu;
    private Icon deleteItem;
    private Icon cancelAction;
    private Icon gamemodes;
    private Icon generators;
    private Icon hooks;
    private Icon stats;
    private Icon edit;

    public ArenaManagmentInvs(DeadByDaylight main) {
        this.main = main;
    }

    public Inventory showMainPage() {
        CustomHolder mainInv = invBuilder.createNew("Arena Manager", 9);
        mainInv.setIcon(0, listArenas);
        mainInv.setIcon(8, closeMenu);
        return mainInv.getInventory();
    }

    public Inventory showArenaList() {
        CustomHolder arenaList = invBuilder.createNew("Arenas:", 45);
        int i = 0;
        for (final DArena arena : main.getArenaManager().getArenas()) {
            Icon icon = invBuilder.createIcon(itemBuilder.getNewBuilder(Material.EMPTY_MAP).build(), arena.getId())
                    .addClickAction(p -> p.openInventory(showArenaPage(arena)));
            arenaList.setIcon(i, icon);
            i++;
        }
        return arenaList.getInventory();
    }

    public Inventory showArenaPage(final DArena arena) {
        CustomHolder arenaInv = invBuilder.createNew(arena.getId(), 9);
        arenaInv.setIcon(8, closeMenu);
        Icon del = deleteItem.getCopy().clearLore();
        arenaInv.setIcon(7, del.addClickAction(p -> p.openInventory(confirmDelete(arena))));

        int usableModes = arena.getUsableModes().size();
        int totalModes = main.getGamemodeManager().getGamemodes().size();

        Icon gm = gamemodes.getCopy().clearLore()
                .addLore("Using " + usableModes + "/" + totalModes + " modes").addClickAction(p -> {
                    p.closeInventory();
                    main.getArenaManager().setUsableGamemodes(arena);
                    p.openInventory(showGamemodesPage(arena));
                });
        Icon gn = generators.getCopy().clearLore()
                .addLore("Has " + arena.getPossibleGeneratorLocations().size() + " generators");
        Icon h = hooks.getCopy().clearLore().addLore("Has " + arena.getPossibleHookLocations().size() + " hooks");
        String loc = (arena.getLobbyLocation() != null) ?
                ((Double) arena.getLobbyLocation().getX()).intValue() + "...," +
                        ((Double) arena.getLobbyLocation().getY()).intValue() + "...," +
                        ((Double) arena.getLobbyLocation().getZ()).intValue() +
                        "... (" + arena.getLobbyLocation().getWorld().toString() + ")" : "N/A";
        Icon s = stats.getCopy().clearLore().addLore("In-use: " + arena.isInUse()).addLore("Lobby Location: " + loc)
                .addLore("Enabled: " + arena.isUsable()).addLore("Click to change settings").addClickAction(p -> {
                    p.closeInventory();
                    p.openInventory(showArenaSettingsPage(arena));
                });
        Icon e = edit.getCopy().clearLore().addClickAction(p -> {
            p.closeInventory();
            p.performCommand("arena edit " + arena.getId());
        });

        arenaInv.setIcon(0, s);
        arenaInv.setIcon(2, gm);
        arenaInv.setIcon(3, gn);
        arenaInv.setIcon(4, h);
        arenaInv.setIcon(6, e);

        return arenaInv.getInventory();
    }

    private String color(int object, int value) {
        if (object >= value) {
            return ChatColor.GREEN + "✓ ";
        }

        return ChatColor.RED + "✕ ";
    }

    public Inventory showArenaSettingsPage(final DArena arena) {
        CustomHolder settingsInv = invBuilder.createNew(arena.getId() + " Settings", 9);
        Icon enabled = arena.isUsable() ? invBuilder.createIcon(itemBuilder.getNewBuilder(Material.WOOL)
                .setByte((short) 5).build(), "&2Enabled") :
                invBuilder.createIcon(itemBuilder.getNewBuilder(Material.WOOL)
                        .setByte((short) 14).build(), "&4Disabled");
        enabled.addClickAction((Player p) -> {
            if (arena.getLobbyLocation() != null) {
                arena.setUsable(!arena.isUsable());
                p.openInventory(showArenaSettingsPage(arena));
            }
        });
        if (arena.getLobbyLocation() == null) {
            enabled.addLore(ChatColor.RED + "You must set a lobby location first!");
        }
        settingsInv.setIcon(0, enabled);
        Icon back = backMenu.getCopy().addClickAction(p -> p.openInventory(showArenaPage(arena)));
        settingsInv.setIcon(8, back);
        return settingsInv.getInventory();
    }

    public Inventory showGamemodesPage(final DArena a) {
        main.getArenaManager().setUsableGamemodes(a);
        int rows = ((a.getUsableModes().size() / 9) + 1) * 9;

        if (a.getUsableModes().size() == rows) {
            rows += 9;

        }
        if (rows > 54) {
            rows = 54;
        }
        CustomHolder gmInv = invBuilder.createNew(a.getId() + "'s Gamemodes", rows);

        Icon back = backMenu.getCopy().addClickAction(p -> {
            p.closeInventory();
            p.openInventory(showArenaPage(a));
        });

        gmInv.setIcon(rows - 1, back);

        int pos = 0;
        for (final DGamemode mode : a.getUsableModes().keySet()) {

            Icon i;
            if (a.getUsableModes().get(mode)) {
                i = invBuilder.createIcon(itemBuilder.getNewBuilder(Material.WOOL)
                        .setByte((short) 5).build(), ChatColor.GREEN + "✔ " + mode.getId());
                i.addLore(ChatColor.translateAlternateColorCodes('&', "&4Click to &4&lDISABLE &4 this mode!"));
                i.addLore("");
                i.addClickAction(p -> {
                    p.closeInventory();
                    a.getUsableModes().put(mode, false);

                    p.openInventory(showGamemodesPage(a));
                });

            } else {
                i = invBuilder.createIcon(itemBuilder.getNewBuilder(Material.WOOL)
                        .setByte((short) 14).build(), ChatColor.GOLD + "✕ " + mode.getId());
                i.addLore(ChatColor.translateAlternateColorCodes('&', "&aClick to &a&lENABLE &a this mode!"));
                i.addLore("");
                i.addClickAction(p -> {
                    p.closeInventory();
                    a.getUsableModes().put(mode, true);
                    p.openInventory(showGamemodesPage(a));
                });
            }

            i.addLore(ChatColor.GREEN + "Required: ");
            i.addLore(color(a.getPossibleGeneratorLocations().size(), mode.getMaxgenerators())
                    + "Generators: " + a.getPossibleGeneratorLocations().size() + "/" + mode.getMaxgenerators());
            i.addLore(color(a.getPossibleChestSpawns().size(), mode.getMaxchests())
                    + "Chests: " + a.getPossibleChestSpawns().size() + "/" + mode.getMaxchests());
            i.addLore(color(a.getPossibleHookLocations().size(), mode.getMaxhooks())
                    + "Hooks: " + a.getPossibleHookLocations().size() + "/" + mode.getMaxhooks());
            i.addLore(color(a.getPossibleHuntedSpawns().size(), mode.getHunted())
                    + "Hunted Spawns: " + a.getPossibleHuntedSpawns().size() + "/" + mode.getHunted());
            i.addLore(color(a.getPossibleHuntedSpawns().size(), mode.getHunters())
                    + "Hunter Spawns: " + a.getPossibleHunterSpawns().size() + "/" + mode.getHunters());

            gmInv.setIcon(pos, i);
            pos++;
        }

        return gmInv.getInventory();
    }

    public Inventory confirmDelete(final DArena arena) {
        CustomHolder confDelete = invBuilder.createNew("Delete Arena: " + arena.getId(), 9);
        Icon del = deleteItem.getCopy().clearLore();
        confDelete.setIcon(8, del.addClickAction((Player p) -> {
            p.performCommand("arena delete " + arena.getId());
            p.openInventory(showArenaList());
        }).addLore("&4This will permanently delete this arena and its data!"));
        confDelete.setIcon(0, cancelAction.addClickAction(p -> p.openInventory(showArenaPage(arena))));

        return confDelete.getInventory();
    }

    public void prepareIcons() {
        listArenas = invBuilder.createIcon(itemBuilder.getNewBuilder(Material.MAP).build(), "&6Arena List")
                .addClickAction(p -> p.performCommand("arena list"));

        closeMenu = invBuilder.createIcon(itemBuilder.getNewBuilder(Material.REDSTONE_BLOCK).build(), "&4Close")
                .addClickAction(HumanEntity::closeInventory);

        backMenu = invBuilder.createIcon(itemBuilder.getNewBuilder(Material.REDSTONE).build(), "&4Back")
                .addClickAction(p -> p.sendMessage("Back one inv"));

        deleteItem = invBuilder.createIcon(itemBuilder.getNewBuilder(Material.CAULDRON_ITEM).build(), "&4Delete");

        cancelAction = invBuilder.createIcon(itemBuilder.getNewBuilder(Material.REDSTONE).build(), "&4Cancel");

        gamemodes = invBuilder.createIcon(itemBuilder.getNewBuilder(Material.WORKBENCH).build(), "Gamemodes");
        generators = invBuilder.createIcon(itemBuilder.getNewBuilder(Material.FURNACE).build(), "Generators");
        hooks = invBuilder.createIcon(itemBuilder.getNewBuilder(Material.TRIPWIRE_HOOK).build(), "Hooks");
        stats = invBuilder.createIcon(itemBuilder.getNewBuilder(Material.BOOK).build(), "&6Stats/Settings");
        edit = invBuilder.createIcon(itemBuilder.getNewBuilder(Material.LEVER).build(), "&2Edit");
    }
}
