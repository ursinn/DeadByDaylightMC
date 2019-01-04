package noahnok.DBDL.files.game;

;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import noahnok.DBDL.files.DeadByDaylight;
import noahnok.DBDL.files.signs.DSign;
import noahnok.DBDL.files.signs.SignStatus;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;


public class MatchMaking {

    private DeadByDaylight main;

    private List<DGame> waitingGames = new ArrayList<DGame>();
    public Map<UUID,BukkitTask> matchMakingLoop = new HashMap<UUID,BukkitTask>();

    public MatchMaking(DeadByDaylight main) {
        this.main = main;
    }

    public void removeGame(DGame game){
        waitingGames.remove(game);
    }


    //Determine here if the player needs to wait for a game or can join to a current one.
    public boolean addToMatchmaking(Player p, String playType){

        //Check if there are any available games
        if (waitingGames.isEmpty()){
            //Try and create a new game
            DGame game = main.getGameManager().createNewGame();
            if (game == null){
                // No games available (Most likely none were setup
                return false;
            }

            //Join player to game with their playType (HUNTED/HUNTER)
            main.getGameManager().joinPlayerToGame(p, game, playType);

            //Tell the plugin that the game needs to be filled up
            waitingGames.add(game);

            //Transfer new game so it is taken care of by the game manager
            transferGame(game);
            return true;
        }else{

            //A game was found
            for (DGame game : waitingGames){

                //Check playtype //TODO swap out set values for gamemode value
                if (playType.equalsIgnoreCase("HUNTER")){
                    if (game.getHunters().size() < 1){


                        main.getGameManager().joinPlayerToGame(p, game, playType);
                        return true;
                    }else{
                        continue;
                    }
                }else{
                    if (game.getHunted().size() < 4){

                        main.getGameManager().joinPlayerToGame(p, game, playType);
                        return true;
                    }else{
                        continue;
                    }
                }
            }

            //No game could be joined. Try and make a new game to prevent players from waiting
            DGame game = main.getGameManager().createNewGame();
            if (game != null){


                //Join player
                main.getGameManager().joinPlayerToGame(p, game, playType);

                //Transfer game over to game manager for handling
                transferGame(game);
                waitingGames.add(game);
                return true;
            }else {
                return false;
            }
        }

    }


    //Dispatches game over to its proper manager. It only stays here temporarily! It should never stay here forever!
    private void transferGame(DGame game){
        main.getGameManager().getGames().add(game);
        DSign signn = null;
        for (DSign sign : main.getSignManager().getSigns()){
            if (sign.getStatus() == SignStatus.IDLE){
                signn = sign;
                break;
            }
        }
        if (signn != null) {
            signn.setGame(game);
            signn.update();
        }
    }

    //Keep players notified with a visual.
    public void addPlayerToMatchMakingLoop(final Player p, final String playType){
        BukkitTask tryMatchMake = new BukkitRunnable(){
            public void run() {
                if (addToMatchmaking(p, playType)){
                    removePlayerFromMatchMakingLoop(p);
                }
                new BukkitRunnable(){
                    public void run() {
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("Searching for game"));
                    }
                }.runTaskLater(main, 0);
                new BukkitRunnable(){
                    public void run() {
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("Searching for game."));
                    }
                }.runTaskLater(main, 5);
                new BukkitRunnable(){
                    public void run() {
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("Searching for game.."));
                    }
                }.runTaskLater(main, 10);
                new BukkitRunnable(){
                    public void run() {
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("Searching for game..."));
                    }
                }.runTaskLater(main, 15);
            }
        }.runTaskTimer(main, 0 ,20);

        matchMakingLoop.put(p.getUniqueId(), tryMatchMake);

    }

    //Remove player. They either left or queued for too long.
    public void  removePlayerFromMatchMakingLoop(Player p){
        matchMakingLoop.get(p.getUniqueId()).cancel();
        matchMakingLoop.remove(p.getUniqueId());

    }

    public void removeGameFromMatchmaking(DGame game){
        waitingGames.remove(game);
    }
}
