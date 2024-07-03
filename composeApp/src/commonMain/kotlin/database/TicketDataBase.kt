package database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Ticket::class],
    version = 1
)
abstract class TicketDataBase : RoomDatabase(), DB {

    abstract fun ticketDao(): TicketDao

    override fun clearAllTables() {
        super.clearAllTables()
    }

}

// Class 'AppDatabase_Impl' is not abstract and does not implement abstract base class member 'clearAllTables'.
interface DB {
    fun clearAllTables() {}
}