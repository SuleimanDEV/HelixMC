package club.helix.hg.player.event;

import club.helix.hg.HgGame;
import club.helix.hg.player.GamePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameEndEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();

    private final HgGame game;
    private final GamePlayer winner;

    public GameEndEvent(HgGame game, GamePlayer winner) {
        this.game = game;
        this.winner = winner;
    }

    public HgGame getGame() {
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
