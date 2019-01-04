package noahnok.DBDL.files.utils.builders;

import noahnok.DBDL.files.DeadByDaylight;
import noahnok.DBDL.files.game.DArena;
import noahnok.DBDL.files.game.DGame;
import noahnok.DBDL.files.game.DGamemode;
import noahnok.DBDL.files.game.STATUS;

public class DGameBuilder {

    public DGame newGame(DArena arena, DGamemode mode, DeadByDaylight main) {
        return new DGame(arena, mode, STATUS.WAITING, main);
    }
}
