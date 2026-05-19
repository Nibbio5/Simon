package com.example.simon

import database.Game
import database.GamesDao
import kotlinx.coroutines.flow.Flow

class GamesRepository (private val gamesDao: GamesDao) {

    val allGames: Flow<List<Game>> = gamesDao.getAllGames()

    suspend fun insert(game: Game){
        gamesDao.insert(game)
    }

}