package database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Ticket(
    val date: String,
    val url: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
