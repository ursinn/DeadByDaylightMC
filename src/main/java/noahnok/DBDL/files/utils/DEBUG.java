package noahnok.dbdl.files.utils;

import noahnok.dbdl.files.DeadByDaylight;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class DEBUG {


    private final DeadByDaylight main;

    public DEBUG(DeadByDaylight main) {
        this.main = main;
    }

    public void debug(String message) {


        Bukkit.getConsoleSender().sendMessage("[" + main.getDescription().getName() + "] " + ChatColor.RED + message);
    }

    public void info(String message) {
        main.getLogger().info(message);
    }
}
