package noahnok.DBDL.files.game.levers;

import noahnok.DBDL.files.game.DGame;
import noahnok.DBDL.files.game.ExitGate;
import noahnok.DBDL.files.player.DPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.material.Lever;

public class DLever {

    private final Location loc;
    private final Block block;
    private DGame game;
    private ExitGate gate;

    private int percentDone = 0;
    private boolean finished = false;


    public DLever(Location loc, DGame game, ExitGate gate) {
        this.game = game;
        this.loc = loc;
        this.gate = gate;
        this.block = loc.getBlock();

        block.setType(Material.LEVER);

        BlockState blockState = block.getState();
        Lever lever = (Lever) blockState.getData();

        BlockFace[] blockFaces = {BlockFace.EAST, BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH};
        for (BlockFace bf : blockFaces) {
            Block bloc = block.getRelative(bf);
            if (bloc.getType().equals(Material.IRON_BLOCK)) {

                lever.setFacingDirection(bf.getOppositeFace());
                break;
            }
        }

        blockState.update();


    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void kill() {
        this.game = null;
        this.gate = null;
    }

    public void increment(DPlayer p) {
        percentDone++;
        if (percentDone >= 100) {
            this.finished = true;
            BlockState blockState = block.getState();
            Lever lever = (Lever) blockState.getData();
            lever.setPowered(true);
            blockState.update();
            gate.openGate();
            game.announce("A gate has been opened!");
            game.setCanEscape(true);
            p.setGatesOpened(p.getGatesOpened() + 1);

            return;
        }
        p.addToScore((int) (10 * game.getMultiplier()));

    }


    public int getPercentDone() {
        return percentDone;
    }

    public ExitGate getGate() {
        return gate;
    }

    public Location getLoc() {
        return loc;
    }

    public Block getBlock() {
        return block;
    }
}
