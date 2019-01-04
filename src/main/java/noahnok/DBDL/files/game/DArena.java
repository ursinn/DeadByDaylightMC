package noahnok.DBDL.files.game;

import org.bukkit.Location;

import java.util.*;

public class DArena {

    private String ID;

    private Map<DGamemode, Boolean> usableModes = new HashMap<DGamemode, Boolean>();

    private Set<Location> possibleGeneratorLocations = new HashSet<Location>();
    private Set<Location> possibleHatchLocations = new HashSet<Location>();
    private Set<Location> possibleHuntedSpawns = new HashSet<Location>();
    private Set<Location> possibleHunterSpawns = new HashSet<Location>();
    private Set<Location> possilbeChestSpawns = new HashSet<Location>();
    private Set<ExitGate> exitGateLocations = new HashSet<ExitGate>();
    private Set<Location> possibleHookLocations = new HashSet<Location>();
    private Set<Location> trapLocations = new HashSet<Location>();
    private Set<Location> cabinetLocations = new HashSet<Location>();
    private Set<Location> exitArea = new HashSet<Location>();
    private Location lobbyLocation;

    private boolean inUse = false;
    private boolean usable = false;

    public boolean isUsable() {
        return usable;
    }

    public void setUsable(boolean usable) {
        this.usable = usable;
    }

    public boolean isInUse() {
        return inUse;
    }


    public void setExitArea(Set<Location> exitArea) {
        this.exitArea = exitArea;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

    public DArena (String arenaID){
        this.ID = arenaID;

    }

    public String getID() {
        return ID;
    }


    public Set<Location> getExitArea() {
        return exitArea;
    }

    public Set<Location> getPossibleGeneratorLocations() {
        return possibleGeneratorLocations;
    }

    public Set<Location> getPossibleHatchLocations() {
        return possibleHatchLocations;
    }

    public Set<Location> getPossibleHuntedSpawns() {
        return possibleHuntedSpawns;
    }

    public Set<Location> getPossibleHunterSpawns() {
        return possibleHunterSpawns;
    }

    public Set<Location> getPossilbeChestSpawns() {
        return possilbeChestSpawns;
    }

    public Set<ExitGate> getExitGateLocations() {
        return exitGateLocations;
    }

    public Set<Location> getPossibleHookLocations() {
        return possibleHookLocations;
    }

    public Set<Location> getTrapLocations() {
        return trapLocations;
    }

    public Set<Location> getCabinetLocations() {
        return cabinetLocations;
    }

    public Location getLobbyLocation() {
        return lobbyLocation;
    }



    public void setPossibleGeneratorLocations(Set<Location> possibleGeneratorLocations) {
        this.possibleGeneratorLocations = possibleGeneratorLocations;
    }

    public void setPossibleHatchLocations(Set<Location> possibleHatchLocations) {
        this.possibleHatchLocations = possibleHatchLocations;
    }

    public void setPossibleHuntedSpawns(Set<Location> possibleHuntedSpawns) {
        this.possibleHuntedSpawns = possibleHuntedSpawns;
    }

    public void setPossibleHunterSpawns(Set<Location> possibleHunterSpawns) {
        this.possibleHunterSpawns = possibleHunterSpawns;
    }

    public void setPossilbeChestSpawns(Set<Location> possilbeChestSpawns) {
        this.possilbeChestSpawns = possilbeChestSpawns;
    }

    public void setExitGateLocations(Set<ExitGate> exitGateLocations) {
        this.exitGateLocations = exitGateLocations;
    }

    public void setPossibleHookLocations(Set<Location> possibleHookLocations) {
        this.possibleHookLocations = possibleHookLocations;
    }

    public void setTrapLocations(Set<Location> trapLocations) {
        this.trapLocations = trapLocations;
    }

    public void setCabinetLocations(Set<Location> cabinetLocations) {
        this.cabinetLocations = cabinetLocations;
    }

    public void setLobbyLocation(Location lobbyLocation) {
        this.lobbyLocation = lobbyLocation;
    }

    public Map<DGamemode, Boolean> getUsableModes() {
        return usableModes;
    }
}
