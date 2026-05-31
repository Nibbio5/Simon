package database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * This class is used to create the database for the games
 */
@Database(
    entities = [Game::class],
    version = 1
)
abstract class GamesDatabase : RoomDatabase (){

    /**
     * This function is used to get the dao of the games database
     */
    abstract fun gamesDao(): GamesDao

    /**
     * This companion object is used to create the database
     * and to get the instance of the database
     */
    companion object {

        @Volatile
        private var INSTANCE: GamesDatabase? = null

        fun getDatabase(context: Context): GamesDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GamesDatabase::class.java,
                    "games.db"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}