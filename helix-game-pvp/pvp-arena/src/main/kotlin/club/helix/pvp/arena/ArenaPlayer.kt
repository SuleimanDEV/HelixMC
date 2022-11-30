package club.helix.pvp.arena

import club.helix.pvp.arena.kit.ArenaKit
import club.helix.pvp.arena.kit.KitHandler

class ArenaPlayer(val name: String) {
    var pvp: Boolean = false
    val selectedKits: MutableMap<Int, ArenaKit> = mutableMapOf(Pair(1, ArenaKit.NONE), Pair(2, ArenaKit.NONE))

    fun setSelectedKit(index: Int, kit: ArenaKit) = selectedKits.put(index, kit)
    fun hasSelectedKit(index: Int) = selectedKits.contains(index)
    fun hasSelectedRemainingKit(index: Int, kit: ArenaKit) =
        selectedKits.any { it.key != index && it.value == kit}
    fun getSelectedKit(index: Int) = selectedKits[index]
    fun hasSelectedKit(kit: ArenaKit) = selectedKits.containsValue(kit)
    fun hasSelectedKit(handler: KitHandler) = selectedKits.values.any { it.handler == handler }
    fun hasSelectedKit(index: Int, kit: ArenaKit) = selectedKits.any { it.key == index && it.value == kit }
}