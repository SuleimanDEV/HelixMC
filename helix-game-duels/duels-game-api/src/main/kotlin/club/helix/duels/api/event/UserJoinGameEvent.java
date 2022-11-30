package club.helix.duels.api.event;

import club.helix.duels.api.game.DuelsGame;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UserJoinGameEvent <T extends DuelsGame> extends Event {

    private static final HandlerList handlerList = new HandlerList();

    private final Player player;
    private final T game;

    public UserJoinGameEvent(Player player, T game) {
        this.player = player;
        this.game = game;
    }

    public Player getPlayer() {
        return player;
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
