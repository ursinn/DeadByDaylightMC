package noahnok.DBDL.files.utils;

;
import noahnok.DBDL.files.DeadByDaylight;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class DEBUG {


    private DeadByDaylight main;

    public DEBUG(DeadByDaylight main) {
        this.main = main;
    }

    public void debug(String message){


        Bukkit.getConsoleSender().sendMessage("["+main.getDescription().getName()+"] " + ChatColor.RED + message);
    }

    public void info(String message){
        main.getLogger().info(message);
    }
}
