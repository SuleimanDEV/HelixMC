package club.helix.inv;

import org.bukkit.plugin.java.JavaPlugin;

public class HelixInvsPlugin {

    private static final HelixInvsPlugin instance = new HelixInvsPlugin();
    private static InventoryManager invManager;

    public void register(JavaPlugin plugin) {
        invManager = new InventoryManager(plugin);
        invManager.init();
    }

    public static InventoryManager manager() { return invManager; }
    public static HelixInvsPlugin instance() { return instance; }

}
