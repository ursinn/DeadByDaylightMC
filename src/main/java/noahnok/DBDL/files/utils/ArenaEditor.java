package noahnok.dbdl.files.utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import noahnok.dbdl.files.DeadByDaylight;
import noahnok.dbdl.files.game.DArena;
import noahnok.dbdl.files.game.ExitGate;
import noahnok.dbdl.files.utils.EditorItem.EditorItem;
import noahnok.dbdl.files.utils.EditorItem.ItemExecutor;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class ArenaEditor {

    private final DeadByDaylight main;
    private final Map<UUID, ItemStack[]> pInv = new HashMap<UUID, ItemStack[]>();
    private final Map<UUID, ItemStack[]> pArmour = new HashMap<UUID, ItemStack[]>();
    private final Map<UUID, BukkitTask> tasks = new HashMap<UUID, BukkitTask>();
    private final Set<Shulker> shulkers = new HashSet<Shulker>();
    public Map<UUID, DArena> editing = new HashMap<UUID, DArena>();
    public List<EditorItem> editorItems = new ArrayList<EditorItem>();
    private EditorItem generator, hook, chest, cabinet, hunter, hunted, exit, trap, hatch, exitArea;

    public ArenaEditor(DeadByDaylight main) {
        this.main = main;
    }

    public void addShulker(Location loc, String COLOR, Material m, DArena a) {
        Shulker shulker = loc.getWorld().spawn(loc, Shulker.class);
        shulker.setAI(false);
        shulker.setCollidable(false);
        shulker.setCustomName("DBDL-SHULKER-" + a.getID());
        shulker.setCustomNameVisible(false);
        shulker.setGravity(false);
        assignTeam(shulker, COLOR);
        shulker.setGlowing(true);
        shulker.setInvulnerable(true);
        shulker.setSilent(true);
        shulker.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1));
        shulkers.add(shulker);
        loc.getBlock().setType(m);
        if (COLOR == "BLACK") {
            loc.getBlock().setData((byte) 15);
        }
    }

    public void removeShulker(Location loc, DArena a) {
        Collection<Entity> entities = loc.clone().add(0.5, 0, 0.5).getWorld().getNearbyEntities(loc, 1, 1, 1);
        Shulker shulker = null;
        for (Entity entity : entities) {
            if (entity.getCustomName() != null && entity.getCustomName().equalsIgnoreCase("DBDL-SHULKER-" + a.getID())) {
                shulker = (Shulker) entity;
                break;
            }
        }
        if (shulker == null) return;

        unassignTeam(shulker);
        main.getServer().getEntity(shulker.getUniqueId()).remove();
        shulker.remove();
        shulkers.remove(shulker);

        loc.getBlock().setType(Material.AIR);

    }

    public void toggleEditing(Player p, String arena) {
        DArena a = main.getArenaManager().isArena(arena);
        if (a == null) {
            p.sendMessage("Not a valid arena!");
            return;
        }

        if (!editing.containsKey(p.getUniqueId())) {
            for (DArena ar : editing.values()) {
                if (ar.getID().equals(arena)) {
                    for (UUID key : editing.keySet()) {
                        if (editing.containsKey(key)) {
                            p.sendMessage(main.getServer().getPlayer(key).getName() + " is currently editing this arena!");
                            return;
                        }
                    }
                }

            }
        }

        if (editing.containsKey(p.getUniqueId())) {
            stopEditing(p);
            p.sendMessage("You have stopped editing!");
        } else {
            startEditing(p, a);
            p.sendMessage("You have started editing");
        }
    }

    private void startEditing(final Player p, DArena arena) {
        editing.put(p.getUniqueId(), arena);
        pInv.put(p.getUniqueId(), p.getInventory().getContents());
        pArmour.put(p.getUniqueId(), p.getInventory().getArmorContents());
        p.getInventory().clear();
        for (EditorItem item : editorItems) {
            p.getInventory().addItem(item.getItem());
        }

        p.setGameMode(GameMode.CREATIVE);
        final String msg = "&7You are editing arena: &8" + arena.getID();
        BukkitTask run = new BukkitRunnable() {
            public void run() {
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', msg)));
            }
        }.runTaskTimer(main, 0, 80);
        tasks.put(p.getUniqueId(), run);
        showArenaBlocks(arena);


    }

    public void stopEditing(Player p) {
        hideArenaBlocks(editing.get(p.getUniqueId()));
        main.getArenaManager().setUsableGamemodes(editing.get(p.getUniqueId()));
        tasks.get(p.getUniqueId()).cancel();
        editing.remove(p.getUniqueId());
        p.getInventory().setContents(pInv.get(p.getUniqueId()));
        p.getInventory().setArmorContents(pArmour.get(p.getUniqueId()));
        pInv.remove(p.getUniqueId());
        pArmour.remove(p.getUniqueId());
    }

    private void hideArenaBlocks(final DArena a) {
        for (Location loc : a.getPossibleGeneratorLocations()) {
            removeShulker(loc, a);
        }

        for (Location loc : a.getPossibleHookLocations()) {
            removeShulker(loc, a);
        }

        for (Location loc : a.getPossilbeChestSpawns()) {
            removeShulker(loc, a);
        }

        for (Location loc : a.getCabinetLocations()) {
            removeShulker(loc, a);
        }

        for (Location loc : a.getPossibleHuntedSpawns()) {
            removeShulker(loc, a);
        }

        for (Location loc : a.getPossibleHunterSpawns()) {
            removeShulker(loc, a);
        }

        for (ExitGate gate : a.getExitGateLocations()) {
            for (Location loc : gate.getLocs()) {
                removeShulker(loc, a);
            }
        }

        for (Location loc : a.getExitArea()) {
            removeShulker(loc, a);
        }

        for (Location loc : a.getPossibleHatchLocations()) {
            removeShulker(loc, a);
        }

        for (Location loc : a.getTrapLocations()) {
            removeShulker(loc, a);
        }

        for (World world : main.getServer().getWorlds()) {
            for (Entity entity : world.getEntitiesByClass(Shulker.class)) {
                if (entity.getCustomName().equalsIgnoreCase("DBDL-SHULKER-" + a.getID())) {
                    entity.remove();
                }

            }
        }

    }

    private void showArenaBlocks(final DArena a) {
        for (Location loc : a.getPossibleGeneratorLocations()) {
            addShulker(loc, "BLUE", Material.FURNACE, a);
        }

        for (Location loc : a.getPossibleHookLocations()) {
            addShulker(loc, "RED", Material.IRON_BLOCK, a);
        }

        for (Location loc : a.getPossilbeChestSpawns()) {
            addShulker(loc, "D-BLUE", Material.CHEST, a);
        }

        for (Location loc : a.getCabinetLocations()) {
            addShulker(loc, "L-RED", Material.BOOKSHELF, a);
        }

        for (Location loc : a.getPossibleHuntedSpawns()) {
            addShulker(loc, "WHITE", Material.WOOL, a);
        }

        for (Location loc : a.getPossibleHunterSpawns()) {
            addShulker(loc, "BLACK", Material.WOOL, a);
        }

        for (ExitGate gate : a.getExitGateLocations()) {
            for (Location loc : gate.getLocs()) {
                addShulker(loc, "GRAY", Material.IRON_FENCE, a);
            }
        }

        for (Location loc : a.getExitArea()) {
            addShulker(loc, "GRAY", Material.BARRIER, a);
        }

        for (Location loc : a.getPossibleHatchLocations()) {
            addShulker(loc, "PURPLE", Material.TRAP_DOOR, a);
        }

        for (Location loc : a.getTrapLocations()) {
            addShulker(loc, "PINK", Material.PISTON_BASE, a);
        }

    }

    public void setUpItems() {
        generator = new EditorItem(new ItemStack(Material.FURNACE, 1), "&bGenerator").addExecutor(new ItemExecutor() {
            public void execute(Player p, Location bloc) {
                p.sendMessage("Generator placed!");
                editing.get(p.getUniqueId()).getPossibleGeneratorLocations().add(bloc);
                addShulker(bloc, "BLUE", Material.FURNACE, editing.get(p.getUniqueId()));
            }
        });
        editorItems.add(generator);

        //Hook item
        hook = new EditorItem(new ItemStack(Material.IRON_BLOCK, 1), "&4Hook").addExecutor(new ItemExecutor() {

            public void execute(Player p, Location bloc) {
                p.sendMessage("Hook placed!");
                editing.get(p.getUniqueId()).getPossibleHookLocations().add(bloc);
                addShulker(bloc, "RED", Material.IRON_BLOCK, editing.get(p.getUniqueId()));
            }
        });

        editorItems.add(hook);

        chest = new EditorItem(new ItemStack(Material.CHEST, 1), "&1Chest").addExecutor(new ItemExecutor() {
            public void execute(Player p, Location bloc) {
                p.sendMessage("Chest placed!");
                editing.get(p.getUniqueId()).getPossilbeChestSpawns().add(bloc);
                addShulker(bloc, "D-BLUE", Material.CHEST, editing.get(p.getUniqueId()));
            }
        });

        editorItems.add(chest);

        cabinet = new EditorItem(new ItemStack(Material.BOOKSHELF, 1), "&cCabinet").addExecutor(new ItemExecutor() {
            public void execute(Player p, Location bloc) {
                p.sendMessage("Cabinet Placed");
                editing.get(p.getUniqueId()).getCabinetLocations().add(bloc);
                addShulker(bloc, "L-RED", Material.BOOKSHELF, editing.get(p.getUniqueId()));
            }
        });

        editorItems.add(cabinet);

        hunted = new EditorItem(new ItemStack(Material.WOOL, 1, (short) 0), "&fHunted").addExecutor(new ItemExecutor() {
            public void execute(Player p, Location bloc) {
                p.sendMessage("Hunted Spawn Placed");
                editing.get(p.getUniqueId()).getPossibleHuntedSpawns().add(bloc);
                addShulker(bloc, "WHITE", Material.WOOL, editing.get(p.getUniqueId()));
            }
        });

        editorItems.add(hunted);

        hunter = new EditorItem(new ItemStack(Material.WOOL, 1, (short) 15), "&0Hunter").addExecutor(new ItemExecutor() {
            public void execute(Player p, Location bloc) {
                p.sendMessage("Hunter Spawn Placed");
                editing.get(p.getUniqueId()).getPossibleHunterSpawns().add(bloc);
                addShulker(bloc, "BLACK", Material.WOOL, editing.get(p.getUniqueId()));
            }
        });

        editorItems.add(hunter);

        exit = new EditorItem(new ItemStack(Material.IRON_FENCE, 1), "&8Exit").addExecutor(new ItemExecutor() {
            public void execute(Player p, Location bloc) {
                p.sendMessage("Exit Placed");
                String dir = "";


                double rotation = (p.getLocation().getYaw() - 90) % 360;
                if ((rotation > -45 && rotation < 45) || (rotation > 135 && rotation < 180)) {
                    dir = "EAST";
                } else {
                    dir = "NORTH";
                }

                ExitGate gate = new ExitGate(dir, bloc.clone().add(0, 1, 0), main);
                gate.setLocs(getExitGateLocation(bloc, dir));
                editing.get(p.getUniqueId()).getExitGateLocations().add(gate);
                for (Location loc : gate.getLocs()) {
                    addShulker(loc, "GRAY", Material.IRON_FENCE, editing.get(p.getUniqueId()));
                }


            }
        });

        editorItems.add(exit);

        hatch = new EditorItem(new ItemStack(Material.TRAP_DOOR, 1), "&5Hatch").addExecutor(new ItemExecutor() {
            public void execute(Player p, Location bloc) {
                p.sendMessage("Hatch Location Placed");
                editing.get(p.getUniqueId()).getPossibleHatchLocations().add(bloc);
                addShulker(bloc, "PURPLE", Material.TRAP_DOOR, editing.get(p.getUniqueId()));
            }
        });

        editorItems.add(hatch);

        trap = new EditorItem(new ItemStack(Material.PISTON_BASE, 1), "&dTrap").addExecutor(new ItemExecutor() {
            public void execute(Player p, Location bloc) {
                p.sendMessage("Trap Location Placed");
                editing.get(p.getUniqueId()).getTrapLocations().add(bloc);
                addShulker(bloc, "PINK", Material.PISTON_BASE, editing.get(p.getUniqueId()));
            }
        });

        editorItems.add(trap);

        exitArea = new EditorItem(new ItemStack(Material.BARRIER, 1), "&8Exit Area").addExecutor(new ItemExecutor() {
            public void execute(Player p, Location bloc) {
                p.sendMessage("ExitArea Location Placed");
                editing.get(p.getUniqueId()).getExitArea().add(bloc);
                addShulker(bloc, "PINK", Material.BARRIER, editing.get(p.getUniqueId()));
            }
        });

        editorItems.add(exitArea);


    }

    private List<Location> getExitGateLocation(Location loc, String dir) {

        List<Location> locs = new ArrayList<Location>();
        if (dir.equalsIgnoreCase("EAST")) {
            for (int i = -1; i < 2; i++) {
                for (int y = 0; y < 3; y++) {
                    locs.add(loc.clone().add(0, y, i));
                }
            }
        } else {
            for (int i = -1; i < 2; i++) {
                for (int y = 0; y < 3; y++) {
                    locs.add(loc.clone().add(i, y, 0));
                }
            }
        }

        return locs;

    }

    public void setupShulkerTeams() {
        try {
            unregisterTeam("BLUE");
            unregisterTeam("RED");
            unregisterTeam("D-BLUE");
            unregisterTeam("L-RED");
            unregisterTeam("WHITE");
            unregisterTeam("BLACK");
            unregisterTeam("GRAY");
            unregisterTeam("PURPLE");
            unregisterTeam("PINK");
        } catch (NullPointerException e) {
            main.getLogger().info("Didn't need to unregister teams (Due to restart) <- This is not an error!");
        }
        registerTeam("BLUE", ChatColor.AQUA);
        registerTeam("RED", ChatColor.DARK_RED);
        registerTeam("D-BLUE", ChatColor.DARK_BLUE);
        registerTeam("L-RED", ChatColor.RED);
        registerTeam("WHITE", ChatColor.WHITE);
        registerTeam("BLACK", ChatColor.BLACK);
        registerTeam("GRAY", ChatColor.GRAY);
        registerTeam("PURPLE", ChatColor.DARK_PURPLE);
        registerTeam("PINK", ChatColor.LIGHT_PURPLE);
    }

    private void registerTeam(String color, ChatColor col) {
        main.sbrd.registerNewTeam("DBDL-SH-" + color);
        main.sbrd.getTeam("DBDL-SH-" + color).setPrefix(col + "");

    }

    private void unregisterTeam(String color) {
        main.sbrd.getTeam("DBDL-SH-" + color).unregister();
    }

    private void assignTeam(Shulker shulker, String color) {
        main.sbrd.getTeam("DBDL-SH-" + color).addEntry(shulker.getUniqueId().toString());
    }

    private void unassignTeam(Shulker shulker) {
        try {
            main.sbrd.getTeam("DBDL-SH-BLUE").removeEntry(shulker.getUniqueId().toString());
            main.sbrd.getTeam("DBDL-SH-D-BLUE").removeEntry(shulker.getUniqueId().toString());
            main.sbrd.getTeam("DBDL-SH-RED").removeEntry(shulker.getUniqueId().toString());
            main.sbrd.getTeam("DBDL-SH-L-RED").removeEntry(shulker.getUniqueId().toString());
        } catch (NullPointerException e) {

        }
    }


}
