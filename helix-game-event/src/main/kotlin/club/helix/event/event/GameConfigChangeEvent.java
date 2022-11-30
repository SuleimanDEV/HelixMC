package club.helix.event.event;

import club.helix.event.EventGame;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameConfigChangeEvent <T> extends Event {

    private static final HandlerList handlerList = new HandlerList();

    private final EventGame game;
    private final String configName;
    private final T oldValue;
    private T value;

    public GameConfigChangeEvent(EventGame game, String configName, T oldValue, T value) {
        this.game = game;
        this.configName = configName;
        this.oldValue = oldValue;
        this.value = value;
    }

    public EventGame getGame() {
        return game;
    }

    public String getConfigName() {
        return configName;
    }

    public T getOldValue() {
        return oldValue;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        if (value instanceof Boolean && (!(oldValue instanceof Boolean))) {
            throw new NullPointerException("unsupported value");
        }

        this.value = value;
        game.setConfig(configName, value);
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
