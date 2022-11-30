package club.helix.duels.api.event;

import club.helix.duels.api.game.DuelsGame;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class StartingStateTaskEvent extends Event implements Cancellable {

    private static final HandlerList handlerList = new HandlerList();

    private boolean cancelled;
    private final DuelsGame game;

    public StartingStateTaskEvent(DuelsGame game) {
        this.game = game;
    }

    public DuelsGame getGame() {
        return game;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean value) {
        cancelled = value;
    }
}
