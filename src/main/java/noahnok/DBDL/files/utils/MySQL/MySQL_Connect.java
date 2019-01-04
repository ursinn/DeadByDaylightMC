package noahnok.DBDL.files.utils.MySQL;


import noahnok.DBDL.files.DeadByDaylight;
import noahnok.DBDL.files.player.DPlayer;
import noahnok.DBDL.files.utils.DEBUG;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import java.util.UUID;

;

public class MySQL_Connect {

    private Connection connection;
    private String host,database,username,password;
    private int port;


    private DeadByDaylight main;



    private DEBUG debug;


    public MySQL_Connect(DeadByDaylight main) {
        this.main = main;
        debug = new DEBUG(main);
    }

    public void initConnection() {
        loadValues();
        if (!main.getToggles().usingSQL){
            return;
        }
        try{
            if (connection != null && !connection.isClosed()) {
                return;
            }

            synchronized (this) {
                if (connection != null && !connection.isClosed()) {
                    return;
                }
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);

            }
        } catch (SQLException e){
            debug.debug("Failed to connect to MySQL, NO PLAYER RELATED DATA WILL BE SAVED UNTIL YOU CONNECT! Please edit the config.yml and then run /dbdl mysql connect");
            main.getToggles().usingSQL = false;
            return;
        } catch (ClassNotFoundException e){
            debug.debug("Failed to find MySQL Driver, falling back onto FlatFile!");
            main.getToggles().usingSQL = false;
            return;
        }
        if (connection == null){
            main.getToggles().usingSQL = false;
            return;
        }
        checkTableExists();

    }

    public boolean reInitConnection() {
        loadValues();

        try{
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;
            }

            synchronized (this) {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                    connection = null;
                }
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
                checkTableExists();
                main.getToggles().usingSQL = true;
                //Save all player data that may of accumulated!
                for (Player p : main.getServer().getOnlinePlayers()){
                    main.getdPlayerManager().savePlayer(p.getUniqueId());
                }
                return true;
            }
        } catch (SQLException e){
            debug.debug("Failed to connect to MySQL, falling back onto FlatFile! Please edit the config.yml and then run /dbdl mysql connect");
            main.getToggles().usingSQL = false;
            return false;
        } catch (ClassNotFoundException e){
            debug.debug("Failed to find MySQL Driver, falling back onto FlatFile!");
            main.getToggles().usingSQL = false;
            return false;
        }


    }

    public void loadValues(){
        main.reloadConfig();
        this.host = main.getConfig().getString("mysql.host");
        this.port = main.getConfig().getInt("mysql.port");
        this.username = main.getConfig().getString("mysql.username");
        this.password = main.getConfig().getString("mysql.password");
        this.database = main.getConfig().getString("mysql.database");

    }

    public void checkTableExists(){
        new BukkitRunnable(){
            @Override
            public void run() {

                try {
                    Statement s = connection.createStatement();
                    s.execute("CREATE TABLE IF NOT EXISTS `dbdl_user_stats` (" +
                            "  `name` text NOT NULL," +
                            "  `uuid` char(36) NOT NULL," +
                            "  `bloodPoints` int(11) NOT NULL DEFAULT '0'," +
                            "  `score` int(11) DEFAULT '0'," +
                            "  `escapes` int(11) NOT NULL DEFAULT '0'," +
                            "  `sacrificed` int(11) NOT NULL DEFAULT '0'," +
                            "  `deaths` int(11) NOT NULL DEFAULT '0'," +
                            "  `wins` int(11) NOT NULL DEFAULT '0'," +
                            "  `generators_fixed` int(11) NOT NULL DEFAULT '0'," +
                            "  `generators_failed` int(11) NOT NULL DEFAULT '0'," +
                            "  `times_hooked` int(11) NOT NULL DEFAULT '0'," +
                            "  `hook_escapes` int(11) NOT NULL DEFAULT '0'," +
                            "  `heals` int(11) NOT NULL DEFAULT '0'," +
                            "  `currently_status` text," +
                            "  `currently_gamemode` text," +
                            "  `currently_arena` text," +
                            "  UNIQUE (uuid)" +
                            ") ENGINE=InnoDB DEFAULT CHARSET=latin1;");
                    s.closeOnCompletion();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }.runTaskAsynchronously(main);

    }

    public void closeConnection(){

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void modifyUserStats(final Player p, final String stat, final int amount){
        if (!main.getToggles().usingSQL){
            return;
        }
        new BukkitRunnable(){

            public void run() {

                try {
                    Statement statement = connection.createStatement();
                    statement.executeUpdate("UPDATE dbdl_user_stats SET "+stat+"="+amount+" WHERE uuid='"+p.getUniqueId().toString()+"'");
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(main);
    }

    public void uploadUserStats(final DPlayer p){
        if (!main.getToggles().usingSQL){
            return;
        }



        new BukkitRunnable(){

            public void run() {

                try {
                    PreparedStatement ps = connection.prepareStatement("UPDATE dbdl_user_stats SET bloodPoints=?,escapes=?,sacrificed=?,deaths=?,wins=?,generators_fixed=?,generators_failed=?,times_hooked=?,hook_escapes=?,heals=?,score=? WHERE uuid=?");
                    ps.setInt(1,p.getBloodPoints());
                    ps.setInt(2, p.getEscapes());
                    ps.setInt(3, p.getTimesSacrificed());
                    ps.setInt(4, p.getDeaths());
                    ps.setInt(5, p.getWins());
                    ps.setInt(6, p.getGeneratorsFixed());
                    ps.setInt(7, p.getGeneratorsMessedup());
                    ps.setInt(8, p.getTimesHooked());
                    ps.setInt(9, p.getHookEscapes());
                    ps.setInt(10, p.getHeals());
                    ps.setInt(11, p.getScore());
                    ps.setString(12, p.getId().toString());

                    ps.execute();
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(main);
    }

    public void checkPlayerDataExists(final Player p){
        if (!main.getToggles().usingSQL){
            return;
        }
        new BukkitRunnable(){
            public void run() {
                try {
                    Statement statement = connection.createStatement();
                    statement.executeUpdate("INSERT INTO dbdl_user_stats (uuid,name) VALUES ('"+p.getUniqueId().toString()+"','"+p.getName()+"')");
                    statement.close();
                } catch (SQLException e) {

                }
            }
        }.runTaskAsynchronously(main);
    }

    public void loadPlayerData(final Player p){
        if (!main.getToggles().usingSQL || connection == null){
            DPlayer player = new DPlayer(p.getUniqueId(), null, main);
            main.getdPlayerManager().getDPlayers().add(player);
        }
        new BukkitRunnable(){
            public void run() {
                try {
                    Statement statement = connection.createStatement();
                    ResultSet rs = statement.executeQuery("SELECT * FROM dbdl_user_stats WHERE uuid='" + p.getUniqueId().toString() + "'");

                    setPlayerData(p.getUniqueId(), rs);
                    rs.close();
                    statement.close();
                    return;
                } catch (SQLException e){

                    DPlayer player = new DPlayer(p.getUniqueId(), null, main);
                    main.getdPlayerManager().getDPlayers().add(player);
                    return;
                } catch (NullPointerException e){
                    DPlayer player = new DPlayer(p.getUniqueId(), null, main);
                    main.getdPlayerManager().getDPlayers().add(player);
                    return;
                }
            }
        }.runTaskAsynchronously(main);
    }

    public void setPlayerData(UUID id, ResultSet rs){
        if (!main.getToggles().usingSQL){
            return;
        }
        DPlayer player = new DPlayer(id, null, main);
        try {
            if (rs.next()) {
                player.setBloodPoints(rs.getInt("bloodPoints"));
                player.setEscapes(rs.getInt("escapes"));
                player.setTimesSacrificed(rs.getInt("sacrificed"));
                player.setDeaths(rs.getInt("deaths"));
                player.setWins(rs.getInt("wins"));
                player.setGeneratorsFixed(rs.getInt("generators_fixed"));
                player.setGeneratorsMessedup(rs.getInt("generators_failed"));
                player.setTimesHooked(rs.getInt("times_hooked"));
                player.setHookEscapes(rs.getInt("hook_escapes"));
                player.setHeals(rs.getInt("heals"));
                player.setScore(rs.getInt("score"));
                main.getdPlayerManager().getDPlayers().add(player);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

    }



}
