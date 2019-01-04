package noahnok.DBDL.files.utils.builders;

import noahnok.DBDL.files.game.DGame;
import noahnok.DBDL.files.game.ExitGate;
import noahnok.DBDL.files.game.levers.DLever;
import org.bukkit.Location;

public class DLeverBuilder {

    public DLever newLever(Location leverFowardLoc, DGame game, ExitGate gate){
        return new DLever(leverFowardLoc, game, gate);
    }
}
