package club.helix.lobby.login.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UserAuthenticateEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();

    private final Player player;

    public UserAuthenticateEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
