package database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * This class is used to create the entity for the games database
 *
 * @param id is the PK of the entity
 * @param score is the score of the game
 * @param sequence is the sequence of the pressed buttons of the game
 * @param errorIndex is the index corresponding to the wrong press in the sequence
 */
@Entity
data class Game (
    @PrimaryKey (autoGenerate = true) val id: Int = 0,
    val score: Int?,
    val sequence: String,
    val errorIndex: Int = 0
)