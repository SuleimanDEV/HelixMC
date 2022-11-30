package club.helix.duels.api.event;

import club.helix.duels.api.game.DuelsGame;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SpectatorJoinGameEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();

    private final Player player;
    private final DuelsGame game;

    public SpectatorJoinGameEvent(Player player, DuelsGame game) {
        this.player = player;
        this.game = game;
    }

    public Player getPlayer() {
        return player;
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
}
