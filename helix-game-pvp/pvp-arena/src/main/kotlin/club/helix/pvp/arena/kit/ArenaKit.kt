package club.helix.pvp.arena.kit

import club.helix.bukkit.builder.ItemBuilder
import club.helix.pvp.arena.PvPArena
import club.helix.pvp.arena.kit.provider.*
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

enum class ArenaKit(
    val displayName: String,
    val price: Int = 0,
    val icon: ItemStack,
    val description: Array<String>,
    val handler: KitHandler? = null,
    val conflicts: Array<String> = arrayOf()
) {
    NONE(
        "Nenhum",
        icon = ItemStack(Material.BARRIER),
        description = arrayOf("Kit sem habilidades."),
    ),
    ANCHOR(
        "Anchor", 5000,
        ItemStack(Material.ANVIL),
        arrayOf("Não cause e nem receba", "repulsão."),
        Anchor()
    ),
    ARCHER(
        "Archer",
        icon = ItemStack(Material.BOW),
        description = arrayOf("Utilize seu arco e sua", "flecha e acabe com", "seus inimigos."),
        handler = Archer()
    ),
    BERSERKER(
        "Berserker", 10000,
        icon = ItemBuilder().type(Material.SKULL_ITEM).data(2).toItem,
        description = arrayOf("Fique mais forte ao", "derrotar oponentes."),
        handler = Berserker()
    ),
    BOXER(
        "Boxer", 12500,
        ItemStack(Material.STONE_SWORD),
        arrayOf("Receba menos dano e", "dê mais com sua mão."),
        Boxer(),
        arrayOf("BERSERKER", "ANCHOR")
    ),
    CAMEL(
        "Camel", 3700,
        ItemStack(Material.SAND),
        arrayOf("Ganhe vantagens no deserto."),
        Camel()
    ),
    FIREMAN(
        "Fireman", 7500,
        ItemStack(Material.LAVA_BUCKET),
        arrayOf("Não receba nenhum dano", "de fogo."),
        Fireman()
    ),
    FISHERMAN(
        "Fisherman", 4300,
        ItemStack(Material.FISHING_ROD),
        arrayOf("Use sua vara de pesca", "para pescar os seus", "oponentes."),
        Fisherman()
    ),
    FLASH(
        "Flash", 8000,
        ItemStack(Material.REDSTONE_TORCH_ON),
        arrayOf("Teleporte-se instantaneamente", "para onde você estiver olhando."),
        Flash()
    ),
    GLADIATOR(
        "Gladiator", 20000,
        ItemStack(Material.IRON_FENCE),
        arrayOf("Desafie outros jogadores", "em uma arena nos céus."),
        Gladiator()
    ),
    GRANDPA(
        "Grandpa", 7500,
        ItemStack(Material.STICK),
        arrayOf("Empurre jogadores utilizando", "o seu graveto."),
        Grandpa()
    ),
    GRAPPLER(
        "Grappler",
        icon = ItemStack(Material.LEASH),
        description = arrayOf("Agarre-se aos seus inimigos", "e não os solte.")
    ),
    HULK(
        "Hulk", 9500,
        icon = ItemStack(Material.DISPENSER),
        description = arrayOf("Carregue um jogador em cima", "de você e destrua-o."),
        handler = Hulk()
    ),
    KANGAROO(
        "Kangaroo",
        icon = ItemStack(Material.FIREWORK),
        description = arrayOf("Pule como um canguru."),
        handler = Kangaroo()
    ),
    MAGMA(
        "Magma", 8500,
        icon = ItemStack(Material.MAGMA_CREAM),
        description = arrayOf("Coloque fogo em seus", "oponentes ao ataca-los."),
        handler = Magma()
    ),
    MONK(
        "Monk", 9500,
        ItemStack(Material.BLAZE_ROD),
        arrayOf("Desarme seu inimigo."),
        handler = Monk()
    ),
    NEO(
        "Neo", 10000,
        ItemStack(Material.ARROW),
        arrayOf("Seja imune à projéteis", "e Kit Gladiator."),
        Neo()
    ),
    NINJA(
        "Ninja", 15000,
        icon = ItemStack(Material.EMERALD),
        description = arrayOf("Ataque seu oponente", "pelas costas."),
        handler = Ninja()
    ),
    POSEIDON(
        "Poseidon", 9500,
        ItemStack(Material.WATER_BUCKET),
        arrayOf("Fique mais forte ao", "entrar na água."),
        Poseidon()
    ),
    SNAIL(
        "Snail", 8500,
        ItemStack(Material.SOUL_SAND),
        arrayOf("Cause lentidão ao atacar", "inimigos."),
        Snail()
    ),
    SPECIALIST(
        "Specialist", 15000,
        ItemStack(Material.BOOK),
        arrayOf("Utilize seu encantador", "portátil e encante itens", "matando jogadores."),
    ),
    STEELHEAD(
        "SteelHead", 10000,
        ItemStack(Material.GOLD_HELMET),
        arrayOf("Seja imune à Kit Stomper."),
        handler = SteelHead()
    ),
    STOMPER(
        "Stomper", 18500,
        ItemStack(Material.IRON_BOOTS),
        arrayOf("Esmaque seus inimigos."),
        handler = Stomper(),
        conflicts = arrayOf("KANGAROO", "FLASH", "GRAPPLER", "STEELHEAD")
    ),
    SWITCHER(
        "Switcher", 7500,
        ItemStack(Material.SNOW_BALL),
        arrayOf("Troque de lugar com", "jogadores com suas bolas", "de neve."),
        Switcher()
    ),
    THOR(
        "Thor",
        icon = ItemStack(Material.WOOD_AXE),
        description = arrayOf("Envie raios e destrua", "seus inimigos."),
        handler = Thor()
    ),
    VAMPIRE(
        "Vampire", 12500,
        ItemStack(Material.REDSTONE),
        arrayOf("Regenere sua vida matando", "oponentes."),
        Vampire()
    ),
    VIPER(
        "Viper", 10000,
        ItemStack(Material.SPIDER_EYE),
        arrayOf("Envenene jogadores até", "a morte."),
        Viper()
    ),
    MILKMAN(
        "Milkman", 9000,
        ItemStack(Material.MILK_BUCKET),
        arrayOf("Receba efeitos ao tomar seu", "leite mágico."),
        Milkman()
    ),
    TURTLE(
        "Turtle", 0,
        ItemStack(Material.DIAMOND_CHESTPLATE),
        arrayOf("Receba menos dano ao se agachar."),
        Turtle()
    ),
    QUICKDROP(
        "Quickdrop", 5400,
        ItemStack(Material.BOWL),
        arrayOf("Drope os potes automaticamente."),
        Quickdrop()
    );

    companion object {
        fun loadListeners(plugin: PvPArena) = values().filter { it.handler != null }.forEach {
            plugin.server.pluginManager.registerEvents(it.handler, plugin)
        }
    }
}