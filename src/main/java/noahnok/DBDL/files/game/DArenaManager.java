package noahnok.DBDL.files.game;

import noahnok.DBDL.files.DeadByDaylight;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;


public class DArenaManager {


    private final DeadByDaylight main;
    private final Set<DArena> arenas = new HashSet<DArena>();

    public DArenaManager(DeadByDaylight main) {
        this.main = main;
    }

    public boolean createArena(String ID) {


        DArena arena = new DArena(ID);

        arena.getUsableModes().put(main.getGamemodeManager().getMode("default"), false);
        arenas.add(arena);
        return true;


    }

    public Set<DArena> getArenas() {
        return this.arenas;
    }

    public DArena getRandomArena() {
        List<DArena> tempArenaList = new ArrayList<DArena>();
        for (DArena arena : arenas) {
            boolean hasDefault = false;
            for (DGamemode mode : arena.getUsableModes().keySet()) {
                if (mode.getID().equalsIgnoreCase("default")) {
                    if (arena.getUsableModes().get(mode)) {
                        hasDefault = true;
                    }
                }
            }
            if (arena.isInUse() != true && arena.isUsable() && hasDefault) {
                tempArenaList.add(arena);
            }
        }
        if (tempArenaList.size() == 0) {
            return null;
        }
        return tempArenaList.get(ThreadLocalRandom.current().nextInt(tempArenaList.size()));
    }

    public boolean removeArena(String ID) {
        for (DArena arena : arenas) {

            if (arena.getID().equals(ID)) {
                arenas.remove(arena);
                return true;
            }
        }
        return false;
    }

    public DArena isArena(String arena) {
        for (DArena a : arenas) {
            if (a.getID().equals(arena)) {
                return a;
            }
        }
        return null;
    }


    public String addGamemode(String arena, String mode) {
        DArena a = isArena(arena);
        if (a == null) {
            return "ERROR_NO_ARENA_FOUND";
        }
        DGamemode gamemode = main.getGamemodeManager().getMode(mode);
        if (gamemode == null) {
            return "ERROR_NO_GAMEMODE_FOUND";
        }

        a.getUsableModes().put(gamemode, false);
        return "GAMEMODE_ADDED";

    }

    public void loadArenasFromFile() {
        if (main.getArenasConfig().getConfig().get("arenas") == null) return;
        for (String key : main.getArenasConfig().getConfig().getConfigurationSection("arenas").getKeys(false)) {
            try {
                String path = "arenas." + key + ".";
                DArena arena = new DArena(key);
                FileConfiguration config = main.getArenasConfig().getConfig();

                List<String> gamemodes = config.getStringList(path + "gamemodes");
                Set<DGamemode> usableModes = new HashSet<DGamemode>();
                for (String mode : gamemodes) {
                    String[] data = mode.split("-enabled=");
                    DGamemode usemode = main.getGamemodeManager().getGamemodeFromString(data[0]);
                    if (usemode != null) {
                        usableModes.add(usemode);
                        if (data[1].equalsIgnoreCase("true")) {
                            arena.getUsableModes().put(usemode, true);
                        } else {
                            arena.getUsableModes().put(usemode, false);
                        }
                    }
                }

                gamemodes = null;


                Set<Location> gens = valid((Set<Location>) config.get(path + "locations.generators"));
                Set<Location> hatch = valid((Set<Location>) config.get(path + "locations.hatch"));
                Set<Location> hunted = valid((Set<Location>) config.get(path + "locations.hunted"));
                Set<Location> hunters = valid((Set<Location>) config.get(path + "locations.hunter"));
                Set<Location> chests = valid((Set<Location>) config.get(path + "locations.chests"));

                Set<ExitGate> gates = new HashSet<ExitGate>();
                try {
                    for (String numb : main.getArenasConfig().getConfig().getConfigurationSection(path + "locations.exitGates").getKeys(false)) {
                        String facing = (String) config.get(path + "locations.exitGates." + numb + ".facing");
                        if (facing.length() == 0 || facing == null) continue;

                        List<Location> locs = (List<Location>) config.get(path + "locations.exitGates." + numb + ".locs");
                        if (locs == null) continue;
                        ExitGate newgate = new ExitGate(facing, (Location) config.get(path + "locations.exitGates." + numb + ".center"), main);
                        newgate.setLocs(locs);
                        gates.add(newgate);
                    }
                } catch (NullPointerException e) {
                    main.getLogger().severe("Failed to load exit gates for arena: " + path + " or their were none!");
                }


                Set<Location> hooks = valid((Set<Location>) config.get(path + "locations.hooks"));
                Set<Location> traps = valid((Set<Location>) config.get(path + "locations.traps"));
                Set<Location> cabinets = valid((Set<Location>) config.get(path + "locations.cabinets"));
                Set<Location> exitArea = valid((Set<Location>) config.get(path + "locations.exitArea"));
                Location lobby = (Location) config.get(path + "locations.lobby");

                arena.setPossibleGeneratorLocations(gens);
                arena.setPossibleHatchLocations(hatch);
                arena.setPossibleHuntedSpawns(hunted);
                arena.setPossibleHunterSpawns(hunters);
                arena.setPossilbeChestSpawns(chests);
                arena.setExitGateLocations(gates);
                arena.setPossibleHookLocations(hooks);
                arena.setTrapLocations(traps);
                arena.setCabinetLocations(cabinets);
                arena.setLobbyLocation(lobby);
                arena.setExitArea(exitArea);
                arena.setUsable((Boolean) config.get(path + "enabled"));


                arenas.add(arena);
                setUsableGamemodes(arena);
            } catch (NullPointerException e) {
                main.getLogger().severe("Failed to load arena: " + key + " either see whats wrong or delete it from the arenas.yml!");
            }
        }

    }

    public Set<Location> valid(Set<Location> set) {
        if (set == null) {
            return new HashSet<Location>();
        } else {
            return set;
        }
    }

    public Set<ExitGate> valide(Set<ExitGate> set) {
        if (set == null) {
            return new HashSet<ExitGate>();
        } else {
            return set;
        }
    }

    public void saveArenasToFile() {
        for (DArena arena : arenas) {
            String path = "arenas." + arena.getID() + ".";
            FileConfiguration config = main.getArenasConfig().getConfig();
            List<String> gamemodeStrings = new ArrayList<String>();
            for (DGamemode mode : arena.getUsableModes().keySet()) {
                boolean enabled = arena.getUsableModes().get(mode);

                gamemodeStrings.add(mode.getID() + "-enabled=" + arena.getUsableModes().get(mode));
            }
            config.set(path + "gamemodes", gamemodeStrings);
            config.set(path + "locations.generators", arena.getPossibleGeneratorLocations());
            config.set(path + "locations.hatch", arena.getPossibleHatchLocations());
            config.set(path + "locations.hunted", arena.getPossibleHuntedSpawns());
            config.set(path + "locations.hunter", arena.getPossibleHunterSpawns());
            config.set(path + "locations.chests", arena.getPossilbeChestSpawns());

            config.set(path + "locations.hooks", arena.getPossibleHookLocations());
            config.set(path + "locations.traps", arena.getTrapLocations());
            config.set(path + "locations.cabinets", arena.getCabinetLocations());
            config.set(path + "locations.exitArea", arena.getExitArea());
            config.set(path + "locations.lobby", arena.getLobbyLocation());

            config.set(path + "locations.exitGates", "NoData");
            int i = 0;
            for (ExitGate gate : arena.getExitGateLocations()) {
                config.set(path + "locations.exitGates." + i + ".facing", gate.getFacing());
                config.set(path + "locations.exitGates." + i + ".locs", gate.getLocs());
                config.set(path + "locations.exitGates." + i + ".center", gate.getCenter());
                i++;
            }

            config.set(path + "enabled", arena.isUsable());
        }
        main.getArenasConfig().saveConfig();
    }


    public void setUsableGamemodes(DArena a) {
        for (DGamemode mode : a.getUsableModes().keySet()) {
            if (a.getPossibleGeneratorLocations().size() < mode.getMaxgenerators()) {
                a.getUsableModes().put(mode, false);
                continue;
            }
            if (a.getPossibleHookLocations().size() < mode.getMaxhooks()) {
                a.getUsableModes().put(mode, false);
                continue;
            }
            if (a.getPossilbeChestSpawns().size() < mode.getMaxchests()) {
                a.getUsableModes().put(mode, false);
                continue;
            }
            if (a.getPossibleHuntedSpawns().size() < mode.getHunted()) {
                a.getUsableModes().put(mode, false);
                continue;
            }
            if (a.getPossibleHunterSpawns().size() < mode.getHunters()) {
                a.getUsableModes().put(mode, false);
                continue;
            }

            if (a.getUsableModes().get(mode) != null) {
                a.getUsableModes().put(mode, a.getUsableModes().get(mode));
            } else {

                a.getUsableModes().put(mode, true);
            }
        }
    }

}
