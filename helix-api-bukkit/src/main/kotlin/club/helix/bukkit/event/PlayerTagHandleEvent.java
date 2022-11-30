package club.helix.bukkit.event;

import club.helix.bukkit.nms.NameTagNMS;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerTagHandleEvent extends Event implements Cancellable {

    public static HandlerList handlerList = new HandlerList();

    private boolean cancelled;
    private final Player player;
    private final NameTagNMS.Reason reason;

    public PlayerTagHandleEvent(Player player, NameTagNMS.Reason reason) {
        this.player = player;
        this.reason = reason;
    }

    public Player getPlayer() {
        return player;
    }

    public NameTagNMS.Reason getReason() {
        return reason;
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
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
