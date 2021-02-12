package noahnok.dbdl.files;

import noahnok.dbdl.files.utils.Config;

public class ReadyConfigs {

    private final DeadByDaylight main;

    public ReadyConfigs(DeadByDaylight main) {
        this.main = main;
    }

    public void createConfigs() {
        main.setArenasConfig(new Config("arenas", main));
        main.setGamemodesConfig(new Config("gamemodes", main));
        main.setSignConfig(new Config("signs", main));
        main.setMessagesConfig(new Config("lang", main));
    }
}
