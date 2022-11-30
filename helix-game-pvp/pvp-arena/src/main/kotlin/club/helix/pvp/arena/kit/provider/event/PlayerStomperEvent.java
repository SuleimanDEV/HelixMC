package club.helix.pvp.arena.kit.provider.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerStomperEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Player victim;
    private Boolean cancelled = false;

    public PlayerStomperEvent(Player player, Player victim){
        this.player = player;
        this.victim = victim;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getVictim() {
        return victim;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }
}
