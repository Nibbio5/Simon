package database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Game (
    @PrimaryKey (autoGenerate = true) val id: Int = 0,
    val score: Int?,
    val sequence: String,
    val errorIndex: Int = 0
)