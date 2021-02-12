package noahnok.dbdl.files.game;

import org.bukkit.Location;

import java.util.*;

public class DArena {

    private final String id;

    private final Map<DGamemode, Boolean> usableModes = new HashMap<>();

    private Set<Location> possibleGeneratorLocations = new HashSet<>();
    private Set<Location> possibleHatchLocations = new HashSet<>();
    private Set<Location> possibleHuntedSpawns = new HashSet<>();
    private Set<Location> possibleHunterSpawns = new HashSet<>();
    private Set<Location> possibleChestSpawns = new HashSet<>();
    private Set<ExitGate> exitGateLocations = new HashSet<>();
    private Set<Location> possibleHookLocations = new HashSet<>();
    private Set<Location> trapLocations = new HashSet<>();
    private Set<Location> cabinetLocations = new HashSet<>();
    private Set<Location> exitArea = new HashSet<>();
    private Location lobbyLocation;

    private boolean inUse;
    private boolean usable;

    public DArena(String arenaId) {
        this.id = arenaId;
    }

    public boolean isUsable() {
        return usable;
    }

    public void setUsable(boolean usable) {
        this.usable = usable;
    }

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

    public String getId() {
        return id;
    }

    public Set<Location> getExitArea() {
        return Collections.unmodifiableSet(exitArea);
    }

    public void setExitArea(Set<Location> exitArea) {
        this.exitArea = Collections.unmodifiableSet(exitArea);
    }

    public Set<Location> getPossibleGeneratorLocations() {
        return Collections.unmodifiableSet(possibleGeneratorLocations);
    }

    public void setPossibleGeneratorLocations(Set<Location> possibleGeneratorLocations) {
        this.possibleGeneratorLocations = Collections.unmodifiableSet(possibleGeneratorLocations);
    }

    public Set<Location> getPossibleHatchLocations() {
        return Collections.unmodifiableSet(possibleHatchLocations);
    }

    public void setPossibleHatchLocations(Set<Location> possibleHatchLocations) {
        this.possibleHatchLocations = Collections.unmodifiableSet(possibleHatchLocations);
    }

    public Set<Location> getPossibleHuntedSpawns() {
        return Collections.unmodifiableSet(possibleHuntedSpawns);
    }

    public void setPossibleHuntedSpawns(Set<Location> possibleHuntedSpawns) {
        this.possibleHuntedSpawns = Collections.unmodifiableSet(possibleHuntedSpawns);
    }

    public Set<Location> getPossibleHunterSpawns() {
        return Collections.unmodifiableSet(possibleHunterSpawns);
    }

    public void setPossibleHunterSpawns(Set<Location> possibleHunterSpawns) {
        this.possibleHunterSpawns = Collections.unmodifiableSet(possibleHunterSpawns);
    }

    public Set<Location> getPossibleChestSpawns() {
        return Collections.unmodifiableSet(possibleChestSpawns);
    }

    public void setPossibleChestSpawns(Set<Location> possilbeChestSpawns) {
        this.possibleChestSpawns = Collections.unmodifiableSet(possilbeChestSpawns);
    }

    public Set<ExitGate> getExitGateLocations() {
        return Collections.unmodifiableSet(exitGateLocations);
    }

    public void setExitGateLocations(Set<ExitGate> exitGateLocations) {
        this.exitGateLocations = Collections.unmodifiableSet(exitGateLocations);
    }

    public Set<Location> getPossibleHookLocations() {
        return Collections.unmodifiableSet(possibleHookLocations);
    }

    public void setPossibleHookLocations(Set<Location> possibleHookLocations) {
        this.possibleHookLocations = Collections.unmodifiableSet(possibleHookLocations);
    }

    public Set<Location> getTrapLocations() {
        return Collections.unmodifiableSet(trapLocations);
    }

    public void setTrapLocations(Set<Location> trapLocations) {
        this.trapLocations = Collections.unmodifiableSet(trapLocations);
    }

    public Set<Location> getCabinetLocations() {
        return Collections.unmodifiableSet(cabinetLocations);
    }

    public void setCabinetLocations(Set<Location> cabinetLocations) {
        this.cabinetLocations = Collections.unmodifiableSet(cabinetLocations);
    }

    public Location getLobbyLocation() {
        return lobbyLocation;
    }

    public void setLobbyLocation(Location lobbyLocation) {
        this.lobbyLocation = lobbyLocation;
    }

    public Map<DGamemode, Boolean> getUsableModes() {
        return usableModes;
    }
}
