package club.helix.bukkit.event;

import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Consumer;

public class PlayerDeathEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();

    private final Player player;
    private final Player killer;
    private List<ItemStack> drops;
    private Location dropsLocation;
    private String deathMessage;
    private int droppedExp, newLevel, newExp, newTotalExp;
    private final boolean keepLevel, keepInventory;
    private final Location deathLocation;
    private Consumer<List<Item>> droppedItems;

    public PlayerDeathEvent(Player player, Player killer, List<ItemStack> drops, Location dropsLocation, String deathMessage, int droppedExp, int newLevel, int newExp, int newTotalExp, boolean keepLevel, boolean keepInventory, Location deathLocation) {
        this.player = player;
        this.killer = killer;
        this.drops = drops;
        this.dropsLocation = dropsLocation;
        this.deathMessage = deathMessage;
        this.droppedExp = droppedExp;
        this.newLevel = newLevel;
        this.newExp = newExp;
        this.newTotalExp = newTotalExp;
        this.keepLevel = keepLevel;
        this.keepInventory = keepInventory;
        this.deathLocation = deathLocation;
    }

    public void onDroppedItems(Consumer<List<Item>> consumer) {
        this.droppedItems = consumer;
    }

    public Consumer<List<Item>> getDroppedItems() {
        return droppedItems;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getKiller() {
        return killer;
    }

    public Boolean hasKiller() {
        return killer != null;
    }

    public List<ItemStack> getDrops() {
        return drops;
    }

    public void setDrops(List<ItemStack> drops) {
        this.drops = drops;
    }

    public Location getDropsLocation() {
        return dropsLocation;
    }

    public int getDroppedExp() {
        return droppedExp;
    }

    public Location getDeathLocation() {
        return deathLocation;
    }

    public void setDroppedExp(int droppedExp) {
        this.droppedExp = droppedExp;
    }

    public void setDropsLocation(Location dropsLocation) {
        this.dropsLocation = dropsLocation;
    }

    public int getNewLevel() {
        return newLevel;
    }

    public void setNewLevel(int newLevel) {
        this.newLevel = newLevel;
    }

    public void setNewExp(int newExp) {
        this.newExp = newExp;
    }

    public int getNewExp() {
        return newExp;
    }

    public int getNewTotalExp() {
        return newTotalExp;
    }

    public void setNewTotalExp(int newTotalExp) {
        this.newTotalExp = newTotalExp;
    }

    public boolean isKeepInventory() {
        return keepInventory;
    }

    public boolean isKeepLevel() {
        return keepLevel;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public String getDeathMessage() {
        return deathMessage;
    }

    public void setDeathMessage(String deathMessage) {
        this.deathMessage = deathMessage;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
