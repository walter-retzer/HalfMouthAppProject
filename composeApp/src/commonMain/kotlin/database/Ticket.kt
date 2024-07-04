package database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Ticket(
    val expirationDate: String,
    val discountValue: String,
    val discountName: String,
    val url: String,
    val dateTicketCreated: String,
    val timeTicketCreated: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
