package noahnok.dbdl.files.utils.EditorItem;

import noahnok.dbdl.files.DeadByDaylight;
import noahnok.dbdl.files.game.DArena;
import noahnok.dbdl.files.game.ExitGate;
import org.bukkit.Location;
import org.bukkit.entity.Shulker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.ItemMeta;

public class EditorEvents implements Listener {

    private final DeadByDaylight main;

    public EditorEvents(DeadByDaylight main) {
        this.main = main;
    }

    @EventHandler
    public void editorBlockPlace(BlockPlaceEvent e) {

        if (!main.getArenaEditor().editing.containsKey(e.getPlayer().getUniqueId())) return;
        ItemMeta meta = e.getItemInHand().getItemMeta();
        if (meta != null) {
            if (meta.getLore() != null) {
                if (meta.getLore().get(meta.getLore().size() - 1).replace("§", "").equals("DBDL-EDIT-ITEM")) {
                    EditorItem item = null;

                    for (EditorItem eItem : main.getArenaEditor().editorItems) {

                        if (eItem.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(meta.getDisplayName())) {

                            item = eItem;
                        }
                    }
                    if (item == null) return;

                    for (ItemExecutor exe : item.getExecutors()) {
                        exe.execute(e.getPlayer(), e.getBlock().getLocation());
                    }
                }
            }
        }
    }

    @EventHandler
    public void editorBlockBreak(BlockBreakEvent e) {
        if (!main.getArenaEditor().editing.containsKey(e.getPlayer().getUniqueId())) {


            for (DArena a : main.getArenaManager().getArenas()) {
                if (isArenaBlock(a, e.getBlock().getLocation())) {
                    e.setCancelled(true);
                    e.getPlayer().sendMessage("You cannot break this block as it is in use by an DBDL arena!");
                }
            }
            return;
        }

        if (isArenaBlock(main.getArenaEditor().editing.get(e.getPlayer().getUniqueId()), e.getBlock().getLocation())) {
            main.getArenaEditor().removeShulker(e.getBlock().getLocation(), main.getArenaEditor().editing.get(e.getPlayer().getUniqueId()));
            removeArenaBlock(main.getArenaEditor().editing.get(e.getPlayer().getUniqueId()), e.getBlock().getLocation());
            e.getPlayer().sendMessage("Removed ArenaBlock");
        }
    }

    @EventHandler
    public void editorBlockBreakEntity(PlayerInteractAtEntityEvent e) {
        if (!main.getArenaEditor().editing.containsKey(e.getPlayer().getUniqueId())) {
            Shulker shulker;
            try {
                shulker = (Shulker) e.getRightClicked();
            } catch (ClassCastException exception) {
                return;
            }
            if (shulker == null) return;
            for (DArena a : main.getArenaManager().getArenas()) {
                if (isArenaBlock(a, shulker.getLocation())) {
                    e.setCancelled(true);
                    e.getPlayer().sendMessage("You cannot break this block as it is in use by an DBDL arena!");
                }
            }
            return;
        }
        if (e.getHand() != EquipmentSlot.HAND) return;
        Shulker shulker = (Shulker) e.getRightClicked();
        if (shulker == null) return;
        Location loc = shulker.getLocation();


        Location newloc = loc.clone().subtract(0.5, 0, 0.5);
        newloc.setYaw(0);

        if (loc.getBlock().getType().toString().equalsIgnoreCase("IRON_FENCE")) {

            for (ExitGate gate : main.getArenaEditor().editing.get(e.getPlayer().getUniqueId()).getExitGateLocations()) {
                if (gate.getLocs().contains(newloc)) {

                    for (Location eloc : gate.getLocs()) {
                        main.getArenaEditor().removeShulker(eloc, main.getArenaEditor().editing.get(e.getPlayer().getUniqueId()));
                    }
                    main.getArenaEditor().editing.get(e.getPlayer().getUniqueId()).getExitGateLocations().remove(gate);
                    return;

                }
            }
        }

        main.getArenaEditor().removeShulker(loc, main.getArenaEditor().editing.get(e.getPlayer().getUniqueId()));


        removeArenaBlock(main.getArenaEditor().editing.get(e.getPlayer().getUniqueId()), newloc);
        e.getPlayer().sendMessage("Removed ArenaBlock");


    }

    @EventHandler
    public void preventDrop(PlayerDropItemEvent e) {
        if (!main.getArenaEditor().editing.containsKey(e.getPlayer().getUniqueId())) return;
        e.setCancelled(true);
    }


    private boolean isArenaBlock(DArena a, Location bloc) {
        if (a.getPossilbeChestSpawns().contains(bloc)) return true;
        if (a.getPossibleHunterSpawns().contains(bloc)) return true;
        if (a.getPossibleHuntedSpawns().contains(bloc)) return true;
        if (a.getPossibleHatchLocations().contains(bloc)) return true;
        if (a.getPossibleHookLocations().contains(bloc)) return true;
        if (a.getPossibleGeneratorLocations().contains(bloc)) return true;

        if (a.getCabinetLocations().contains(bloc)) return true;
        if (a.getExitGateLocations().contains(bloc)) return true;
        if (a.getTrapLocations().contains(bloc)) return true;
        return a.getLobbyLocation() == bloc;

    }

    private boolean removeArenaBlock(DArena a, Location bloc) {
        if (a.getPossilbeChestSpawns().remove(bloc)) return true;
        if (a.getPossibleHunterSpawns().remove(bloc)) return true;
        if (a.getPossibleHuntedSpawns().remove(bloc)) return true;
        if (a.getPossibleHatchLocations().remove(bloc)) return true;
        if (a.getPossibleHookLocations().remove(bloc)) return true;
        if (a.getPossibleGeneratorLocations().remove(bloc)) return true;

        if (a.getCabinetLocations().remove(bloc)) return true;
        if (a.getExitGateLocations().remove(bloc)) return true;
        if (a.getTrapLocations().remove(bloc)) return true;
        if (a.getLobbyLocation() == bloc) {
            a.setLobbyLocation(null);
            return true;
        }
        return false;


    }
}
