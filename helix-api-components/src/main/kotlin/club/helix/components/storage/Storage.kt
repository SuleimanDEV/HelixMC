package club.helix.components.storage

interface Storage {

    val newConnection: StorageConnection
    fun createTables()
}