package club.helix.hg.player.event;

import club.helix.hg.HgGame;
import club.helix.hg.player.GamePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GamePlayerJoinEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();

    private final GamePlayer gamePlayer;
    private final HgGame game;

    public GamePlayerJoinEvent(GamePlayer gamePlayer, HgGame game) {
        this.gamePlayer = gamePlayer;
        this.game = game;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public HgGame getGame() {
        return game;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
