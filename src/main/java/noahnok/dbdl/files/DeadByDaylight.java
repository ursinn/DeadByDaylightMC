package noahnok.dbdl.files;

import noahnok.dbdl.files.commands.*;
import noahnok.dbdl.files.game.*;
import noahnok.dbdl.files.game.events.MainEvents;
import noahnok.dbdl.files.game.playerStates.PlayerStateManager;
import noahnok.dbdl.files.game.runnables.NoJump;
import noahnok.dbdl.files.generalEvents.GenericEvents;
import noahnok.dbdl.files.player.DPlayer;
import noahnok.dbdl.files.player.DPlayerManager;
import noahnok.dbdl.files.signs.SignEvents;
import noahnok.dbdl.files.signs.SignManager;
import noahnok.dbdl.files.utils.*;
import noahnok.dbdl.files.utils.EditorItem.EditorEvents;
import noahnok.dbdl.files.utils.MySQL.MySQL_Connect;
import noahnok.dbdl.files.utils.Pagenation.PageEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;

import java.util.UUID;

public class DeadByDaylight extends JavaPlugin {

    public Scoreboard sbrd;

    public String prefix = ChatColor.translateAlternateColorCodes('&', "&8[&7DBDL&8] &7");

    private DArenaManager arenaManager;

    private DGameManager gameManager;

    private DGamemodeManager gamemodeManager;

    private ArenaManagmentInvs arenaInvManager;

    private SetUpDefaults defaults;

    private ArenaEditor arenaEditor;

    private MatchMaking matchMaking;

    private SignManager signManager;

    private DPlayerManager dPlayerManager;

    private MySQL_Connect sqlManager;

    //As far as I know I cant inject these??

    private Config gamemodesConfig;

    private Config arenasConfig;

    private Config signConfig;

    private Config messagesConfig;

    private GenericEvents ge;

    private MainEvents me;

    private SignEvents se;

    private EditorEvents ee;

    private InventoryEvents ie;

    private ReadyConfigs readyConfigs;

    private MainCommands mainCommands;

    private ArenaCommands arenaCommands;

    private JoinGameCommand joinGameCommand;

    private LeaveCommand leaveCommand;

    private Toggles toggles;

    private MessageUtils messageUtils;

    private PlayerStateManager playerStateManager;

    private BukkitTask noJump;

    private StatsCommand statsCommand;

    public JoinGameCommand getJoinGameCommand() {
        return joinGameCommand;
    }

    @Override
    public void onEnable() {
        setAccess();
        saveDefaultConfig();

        this.getCommand("dbdl").setExecutor(mainCommands);

        this.getCommand("stats").setExecutor(statsCommand);

        this.getCommand("arena").setExecutor(arenaCommands);
        this.getCommand("join").setExecutor(joinGameCommand);
        this.getCommand("leave").setExecutor(leaveCommand);
        this.getServer().getPluginManager().registerEvents(ie, this);
        this.getServer().getPluginManager().registerEvents(ee, this);
        this.getServer().getPluginManager().registerEvents(se, this);
        this.getServer().getPluginManager().registerEvents(me, this);
        this.getServer().getPluginManager().registerEvents(ge, this);

        readyConfigs.createConfigs();

        toggles.setUpToggles();

        sqlManager.initConnection();

        sbrd = this.getServer().getScoreboardManager().getMainScoreboard();
        defaults.initialiseBasics();
        arenaManager.loadArenasFromFile();
        signManager.loadSignsFromFile();

        for (Player player : this.getServer().getOnlinePlayers()) {
            dPlayerManager.loadDPlayer(player.getUniqueId());
        }

        this.getServer().getPluginManager().registerEvents(new PageEvent(), this);

        noJump = new NoJump(this).runTaskTimer(this, 0, (20 * 8));
    }

    @Override
    public void onDisable() {
        noJump.cancel();

        for (UUID id : arenaEditor.editing.keySet()) {
            arenaEditor.stopEditing(getServer().getPlayer(id));
            getServer().getPlayer(id).sendMessage("You were forcefully removed from editing due to a reload!");
        }

        // Take players out of running games!
        for (DGame game : gameManager.getGames()) {
            for (DPlayer dplayer : game.getPlayers()) {

                if (dplayer != null || dplayer.getCurrentGame() != null) {
                    getGameManager().removePlayerFromGame(dplayer.getPlayer(), dplayer.getCurrentGame());
                }
            }
        }

        getArenaManager().saveArenasToFile();
        signManager.saveSignsToFile();
        dPlayerManager.savePlayerData();

        sqlManager.closeConnection();
    }

    private void setAccess() {
        arenaManager = new DArenaManager(this);
        gameManager = new DGameManager(this);
        gamemodeManager = new DGamemodeManager(this);
        arenaInvManager = new ArenaManagmentInvs(this);
        defaults = new SetUpDefaults(this);
        arenaEditor = new ArenaEditor(this);
        matchMaking = new MatchMaking(this);
        signManager = new SignManager(this);
        dPlayerManager = new DPlayerManager(this);
        sqlManager = new MySQL_Connect(this);
        ge = new GenericEvents(this);
        me = new MainEvents(this);
        se = new SignEvents(this);
        ee = new EditorEvents(this);
        ie = new InventoryEvents();
        readyConfigs = new ReadyConfigs(this);
        mainCommands = new MainCommands(this);
        arenaCommands = new ArenaCommands(this);
        joinGameCommand = new JoinGameCommand(this);
        leaveCommand = new LeaveCommand(this);
        toggles = new Toggles(this);
        messageUtils = new MessageUtils(this);
        playerStateManager = new PlayerStateManager(this);
        statsCommand = new StatsCommand(this);
    }

    public DArenaManager getArenaManager() {
        return arenaManager;
    }

    public DGameManager getGameManager() {
        return gameManager;
    }

    public DGamemodeManager getGamemodeManager() {
        return gamemodeManager;
    }

    public ArenaManagmentInvs getArenaInvManager() {
        return arenaInvManager;
    }

    public SetUpDefaults getDefaults() {
        return defaults;
    }

    public ArenaEditor getArenaEditor() {
        return arenaEditor;
    }

    public MatchMaking getMatchMaking() {
        return matchMaking;
    }

    public SignManager getSignManager() {
        return signManager;
    }

    public DPlayerManager getdPlayerManager() {
        return dPlayerManager;
    }

    public MySQL_Connect getSqlManager() {
        return sqlManager;
    }

    public Config getGamemodesConfig() {
        return gamemodesConfig;
    }

    public void setGamemodesConfig(Config gamemodesConfig) {
        this.gamemodesConfig = gamemodesConfig;
    }

    public Config getArenasConfig() {
        return arenasConfig;
    }

    public void setArenasConfig(Config arenasConfig) {
        this.arenasConfig = arenasConfig;
    }

    //public Config getMessagesConfig() {
    //return messagesConfig;
    //}

    public Config getMessagesConfig() {
        return messagesConfig;
    }

    public void setMessagesConfig(Config messagesConfig) {
        this.messagesConfig = messagesConfig;
    }

    public Config getSignConfig() {
        return signConfig;
    }

    public void setSignConfig(Config signConfig) {
        this.signConfig = signConfig;
    }

    public Toggles getToggles() {
        return toggles;
    }

    public MessageUtils getMessageUtils() {
        return messageUtils;
    }

    public PlayerStateManager getPlayerStateManager() {
        return playerStateManager;
    }
}
