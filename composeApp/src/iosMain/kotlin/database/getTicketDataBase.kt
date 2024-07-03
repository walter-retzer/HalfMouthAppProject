package database

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import platform.Foundation.NSHomeDirectory

fun getTicketDataBase(): TicketDataBase {
    val dbFile = NSHomeDirectory() + "/ticket.db"
    return Room.databaseBuilder<TicketDataBase>(
        name = dbFile,
        factory = {TicketDataBase::class.instantiateImpl() }
    )
        .setDriver(BundledSQLiteDriver())
        .build()
}