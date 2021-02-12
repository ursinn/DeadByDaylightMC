package noahnok.dbdl.files.utils.builders;

import noahnok.dbdl.files.game.DArena;

public class DArenaBuilder {


    public DArena createDArena(String name) {
        return new DArena(name);
    }
}
