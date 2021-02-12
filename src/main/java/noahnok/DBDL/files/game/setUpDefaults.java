package noahnok.dbdl.files.game;

import noahnok.dbdl.files.DeadByDaylight;

public class setUpDefaults {

    private final DeadByDaylight main;

    public setUpDefaults(DeadByDaylight main) {
        this.main = main;
    }

    public void initialiseBasics() {
        createDefaultGamemode();
        setUpInvIcons();
        main.getGamemodeManager().loadGamemodesFromFile();
        main.getArenaEditor().setUpItems();
        main.getArenaEditor().setupShulkerTeams();

        addDebugValues();
        main.getJoinGameCommand().setUpjoinGameInv();
    }

    private void createDefaultGamemode() {
        DGamemode def = new DGamemode("default", 1, 4, 10, 10, 10, 600, true, true, true, true, true, false, false, true);
        main.getGamemodeManager().addGamemode(def);
    }

    private void addDebugValues() {

    }

    private void setUpInvIcons() {
        main.getArenaInvManager().prepareIcons();
    }

}
