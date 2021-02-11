package noahnok.DBDL.files;

import noahnok.DBDL.files.utils.Config;

;

public class readyConfigs {


    private DeadByDaylight main;

    public readyConfigs(DeadByDaylight main) {
        this.main = main;
    }

    public void createConfigs() {
        main.setArenasConfig(new Config("arenas", main));
        main.setGamemodesConfig(new Config("gamemodes", main));
        main.setSignConfig(new Config("signs", main));
        main.setMessagesConfig(new Config("lang", main));
    }
}
