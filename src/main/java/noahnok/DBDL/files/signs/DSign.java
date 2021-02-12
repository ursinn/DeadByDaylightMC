package noahnok.dbdl.files.signs;

import noahnok.dbdl.files.game.DGame;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

public class DSign {
    private final Block signBlock;
    private final Sign sign;
    private DGame game = null;
    private SignStatus status;

    public DSign(Block block) {
        this.signBlock = block;
        this.sign = (Sign) block.getState();
        this.status = SignStatus.IDLE;
    }

    public void removeGame() {
        this.game = null;
        this.status = SignStatus.IDLE;
        update();
    }

    public DGame getGame() {
        return game;
    }

    public void setGame(DGame game) {
        this.game = game;
        this.status = SignStatus.INUSE;
        update();
    }

    public SignStatus getStatus() {
        return status;
    }

    public void setStatus(SignStatus status) {
        this.status = status;
    }

    public Block getSignBlock() {
        return signBlock;
    }

    public void firstPlace() {
        sign.setLine(0, ChatColor.translateAlternateColorCodes('&', "&8[&7DBDL&8]"));
        sign.setLine(1, ChatColor.RED + "Searching");
        sign.setLine(2, "");
        sign.setLine(3, "");
        sign.update();
    }

    public Sign getSign() {
        return sign;
    }

    public void update() {
        if (status == SignStatus.INUSE) {
            sign.setLine(0, ChatColor.translateAlternateColorCodes('&', "&8[&7DBDL&8]"));
            sign.setLine(1, game.getStatus().text());
            sign.setLine(2, game.getArena().getID());
            sign.setLine(3, game.totalCurrentPlayers() + "/" + game.totalPossiblePlayers() + "");
        } else if (status == SignStatus.IDLE) {
            sign.setLine(0, ChatColor.translateAlternateColorCodes('&', "&8[&7DBDL&8]"));
            sign.setLine(1, ChatColor.RED + "Searching");
            sign.setLine(2, "");
            sign.setLine(3, "");
        }

        sign.update();
    }

}
