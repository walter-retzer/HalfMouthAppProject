package database

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

fun getTicketDataBase(context: Context): TicketDataBase {
    val dbFile = context.getDatabasePath("ticket.db")
    return Room.databaseBuilder<TicketDataBase>(
        context = context.applicationContext,
        name = dbFile.absolutePath
    )
        .setDriver(BundledSQLiteDriver())
        .build()
}
