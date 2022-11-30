package club.helix.event.event;

import club.helix.event.EventGame;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameStateChangeEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();

    private final EventGame game;

    public GameStateChangeEvent(EventGame game) {
        this.game = game;
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
