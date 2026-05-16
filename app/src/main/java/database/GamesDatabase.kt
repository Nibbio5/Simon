package database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Game::class],
    version = 1
)
abstract class GamesDatabase : RoomDatabase (){

    abstract fun gamesDao(): GamesDao

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