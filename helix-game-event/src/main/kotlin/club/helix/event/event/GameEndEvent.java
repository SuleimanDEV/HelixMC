package club.helix.event.event;

import club.helix.event.EventGame;
import club.helix.event.player.GamePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameEndEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();

    private final EventGame game;
    private final GamePlayer winner;

    public GameEndEvent(EventGame game, GamePlayer winner) {
        this.game = game;
        this.winner = winner;
    }

    public EventGame getGame() {
        return game;
    }

    public GamePlayer getWinner() {
        return winner;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
