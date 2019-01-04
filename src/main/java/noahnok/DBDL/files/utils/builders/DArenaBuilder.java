package noahnok.DBDL.files.utils.builders;

import noahnok.DBDL.files.game.DArena;

public class DArenaBuilder {


    public DArena createDArena(String name){
        return new DArena(name);
    }
}
