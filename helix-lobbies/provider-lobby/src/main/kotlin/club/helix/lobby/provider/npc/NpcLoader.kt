package club.helix.lobby.provider.npc

class NpcLoader {
    companion object {
        fun load(vararg npcs: DisplayNpcServer) = npcs.forEach(DisplayNpcServer::load)
    }
}