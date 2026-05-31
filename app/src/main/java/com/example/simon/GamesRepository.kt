package com.example.simon

import database.Game
import database.GamesDao
import kotlinx.coroutines.flow.Flow

/**
 * This class is used to create the repository for the games database
 *
 * @param gamesDao is the dao of the games database
 */
class GamesRepository (private val gamesDao: GamesDao) {

    /**
     * This property is used to get all the games from the database
     */
    val allGames: Flow<List<Game>> = gamesDao.getAllGames()

    /**
     * This function is used to insert a new game in the database
     *
     * @param game is the game needed to insert in the database
     */
    suspend fun insert(game: Game){
        gamesDao.insert(game)
    }

    /**
     * This function is used to get a game by its id from the database
     *
     * @param id is the id of the game needed to get from the database
     */
    fun getGameById(id: Int): Flow<Game> {
        return gamesDao.getGameById(id)

    }

}