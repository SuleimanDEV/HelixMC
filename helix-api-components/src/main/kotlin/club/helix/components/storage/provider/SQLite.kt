package club.helix.components.storage.provider

import club.helix.components.storage.Storage
import club.helix.components.storage.StorageConnection
import java.io.File
import java.sql.DriverManager

class SQLite(private val path: File): Storage {

    override val newConnection: StorageConnection get() = if (path.isDirectory) {
            StorageConnection(DriverManager.getConnection("jdbc:sqlite:$path"))
        } else throw Exception("")

    override fun createTables(): Unit = newConnection.use {
        it.execute("create table if not exists accounts (" +
                "ID int not null auto_increment, " +
                "name text not null," +
                "data json not null, " +
                "primary key (ID))"
        )
        it.execute("create table if not exists clans (" +
                "ID int not null auto_increment, " +
                "name varchar(30) not null, " +
                "data json not null, " +
                "primary key (ID))")
        it.execute("create table if not exists clans (" +
                "ID int not null auto_increment, " +
                "name text not null, " +
                "data json not null, " +
                "primary key (ID))")
    }
}