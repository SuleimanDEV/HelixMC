package club.helix.components.storage

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class StorageConnection(private val connection: Connection): AutoCloseable {

    fun execute(s: String): PreparedStatement? {
        return if (hasConnection) connection.prepareStatement(s).apply{ executeUpdate() } else
            throw SQLException("a conexão não pode ser nula")
    }

    fun query(s: String): ResultSet? {
        return if (hasConnection) connection.prepareStatement(s).executeQuery() else
            throw SQLException("a conexão não pode ser nula")
    }

    private val hasConnection = !connection.isClosed

    override fun close(): Unit = connection.close()
}