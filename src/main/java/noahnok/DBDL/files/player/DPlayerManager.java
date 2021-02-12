package noahnok.dbdl.files.player;

import noahnok.dbdl.files.DeadByDaylight;
import noahnok.dbdl.files.game.DGame;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class DPlayerManager {

    private final DeadByDaylight main;

    private final List<DPlayer> DPlayers = new ArrayList<>();

    public DPlayerManager(DeadByDaylight main) {
        this.main = main;
    }

    public List<DPlayer> getDPlayers() {
        return DPlayers;
    }


    public void loadDPlayer(UUID id) {
        main.getSqlManager().checkPlayerDataExists(main.getServer().getPlayer(id));
        main.getSqlManager().loadPlayerData(main.getServer().getPlayer(id));
    }

    public void savePlayerData() {
        for (DPlayer player : DPlayers) {

            player.kill();

        }
        DPlayers.clear();
    }


    public boolean hasSpectators(DPlayer player) {
        DGame game = player.getCurrentGame();
        for (DPlayer hunted : game.getHunted()) {
            if (hunted.getSpectate() == player) {
                return true;
            }
        }

        return false;
    }

    public List<DPlayer> getSpectators(DPlayer player) {
        List<DPlayer> spectators = new ArrayList<>();
        DGame game = player.getCurrentGame();
        for (DPlayer hunted : game.getHunted()) {
            if (hunted.getSpectate() == player) {
                spectators.add(hunted);
            }
        }

        return spectators;
    }


    public DPlayer getPlayer(UUID id) {
        for (DPlayer player : DPlayers) {
            if (player.getId().equals(id)) {
                return player;
            }
        }
        return null;
    }

    public void savePlayer(UUID id) {
        DPlayer player = getPlayer(id);

        main.getSqlManager().uploadUserStats(player);
    }
}
