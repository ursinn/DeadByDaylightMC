package noahnok.dbdl.files;

import noahnok.dbdl.files.utils.DEBUG;

public class Toggles {

    private final DeadByDaylight main;
    private final DEBUG debug;
    public boolean usingSQL;

    public Toggles(DeadByDaylight main) {
        this.main = main;
        debug = new DEBUG(main);
    }

    public void setUpToggles() {
        if (main.getConfig().getString("storageMethod") != null && main.getConfig().getString("storageMethod").equalsIgnoreCase("mysql")) {
            usingSQL = true;
            debug.info("Using MySQL for player storage!");

        } else {
            usingSQL = false;
            debug.debug("The storage method set in the config was invalid, using FlatFile (.yml) instead!");
        }
    }
}
