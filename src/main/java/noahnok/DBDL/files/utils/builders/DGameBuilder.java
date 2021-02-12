package noahnok.dbdl.files.utils.builders;

import noahnok.dbdl.files.DeadByDaylight;
import noahnok.dbdl.files.game.DArena;
import noahnok.dbdl.files.game.DGame;
import noahnok.dbdl.files.game.DGamemode;
import noahnok.dbdl.files.game.STATUS;

public class DGameBuilder {

    public DGame newGame(DArena arena, DGamemode mode, DeadByDaylight main) {
        return new DGame(arena, mode, STATUS.WAITING, main);
    }
}
