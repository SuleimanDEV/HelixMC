package club.helix.components.storage.provider

import club.helix.components.storage.Storage
import club.helix.components.storage.StorageConnection
import java.sql.DriverManager

class MySQL(
    private val host: String,
    private val user: String,
    private val password: String,
    private val database: String,
    private val port: Int
) : Storage {

    override val newConnection: StorageConnection
    get() = StorageConnection(DriverManager.getConnection(
        "jdbc:mysql://$host:$port/$database?autoReconnect=true&characterEncoding=utf8", user, password
    ))

    override fun createTables(): Unit = newConnection.use {
        it.execute("create table if not exists accounts (" +
                "ID int not null auto_increment, " +
                "name varchar(40) not null," +
                "data json not null, " +
                "primary key (ID))")
        it.execute("create table if not exists punishments (" +
                "ID int not null auto_increment, " +
                "name varchar(40) not null," +
                "data json not null, " +
                "primary key (ID))")
        it.execute("create table if not exists clans (" +
                "ID int not null auto_increment, " +
                "name varchar(30) not null, " +
                "data json not null, " +
                "primary key (ID))")
    }
}