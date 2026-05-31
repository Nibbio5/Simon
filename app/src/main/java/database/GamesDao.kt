package database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

/**
 * This interface is used to create of dao for the games database
 * it contains the different queries for the database
 */
@Dao
interface GamesDao {

    @Upsert
    suspend fun upsert(game: Game)

    @Insert
    suspend fun insert(game: Game)

    @Delete
    suspend fun delete(game: Game)

    @Query("SELECT * FROM game")
    fun getAllGames(): Flow<List<Game>>

    @Query("SELECT * FROM game WHERE id = :id")
    fun getGameById(id: Int): Flow<Game>

}