package club.helix.duels.api.event;

import club.helix.duels.api.game.DuelsGame;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameCreatedEvent <T extends DuelsGame> extends Event {

    private static final HandlerList handlerList = new HandlerList();

    private final T game;

    public GameCreatedEvent(T game) {
        this.game = game;
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
