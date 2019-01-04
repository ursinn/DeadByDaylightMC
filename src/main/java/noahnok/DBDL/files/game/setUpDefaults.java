package noahnok.DBDL.files.game;

;
import noahnok.DBDL.files.DeadByDaylight;
import noahnok.DBDL.files.commands.joinGameCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class setUpDefaults {

    private DeadByDaylight main;

    public setUpDefaults(DeadByDaylight main) {
        this.main = main;
    }

    public void initialiseBasics(){
        createDefaultGamemode();
        setUpInvIcons();
        main.getGamemodeManager().loadGamemodesFromFile();
        main.getArenaEditor().setUpItems();
        main.getArenaEditor().setupShulkerTeams();


        addDebugValues();
        main.getJoinGameCommand().setUpjoinGameInv();


    }

    private void createDefaultGamemode(){
        DGamemode def = new DGamemode("default",1,4,10,10,10,600,true,true,true,true,true,false,false,true);
        main.getGamemodeManager().addGamemode(def);
    }

    private void addDebugValues(){



    }

    private void setUpInvIcons(){
        main.getArenaInvManager().prepareIcons();
    }


}
