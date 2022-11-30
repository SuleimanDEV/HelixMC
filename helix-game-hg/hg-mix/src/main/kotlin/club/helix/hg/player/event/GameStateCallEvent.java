package club.helix.hg.player.event;

import club.helix.hg.HgGame;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameStateCallEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();

    private final HgGame game;

    public GameStateCallEvent(HgGame game) {
        this.game = game;
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
