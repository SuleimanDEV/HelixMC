package club.helix.inv.content;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface InventoryProvider {

    void init(@NotNull Player player, @NotNull InventoryContents contents);
    default void update(@NotNull Player player, @NotNull InventoryContents contents) {}

}
