package noahnok.dbdl.files.utils.builders;

import noahnok.dbdl.files.game.DGame;
import noahnok.dbdl.files.game.ExitGate;
import noahnok.dbdl.files.game.levers.DLever;
import org.bukkit.Location;

public class DLeverBuilder {

    public DLever newLever(Location leverFowardLoc, DGame game, ExitGate gate) {
        return new DLever(leverFowardLoc, game, gate);
    }
}
