package database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Ticket(
    val date: String,
    val discount: String,
    val dateTicketGenerated: String,
    val timeTicketGenerated: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
