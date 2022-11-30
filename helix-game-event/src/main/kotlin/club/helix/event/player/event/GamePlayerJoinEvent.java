package club.helix.event.player.event;

import club.helix.event.EventGame;
import club.helix.event.player.GamePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GamePlayerJoinEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();

    private final GamePlayer gamePlayer;
    private final EventGame game;

    public GamePlayerJoinEvent(GamePlayer gamePlayer, EventGame game) {
        this.gamePlayer = gamePlayer;
        this.game = game;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public EventGame getGame() {
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
