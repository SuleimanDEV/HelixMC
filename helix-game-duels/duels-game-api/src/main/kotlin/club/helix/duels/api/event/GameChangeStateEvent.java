package club.helix.duels.api.event;

import club.helix.duels.api.game.DuelsGame;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameChangeStateEvent<T extends DuelsGame> extends Event {

    private static final HandlerList handlerList = new HandlerList();

    private final T game;
    private final String newState;

    public GameChangeStateEvent(T game, String newState) {
        this.game = game;
        this.newState = newState;
    }

    public String getNewState() {
        return newState;
    }

    public T getGame() {
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
