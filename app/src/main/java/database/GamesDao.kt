package database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

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
}