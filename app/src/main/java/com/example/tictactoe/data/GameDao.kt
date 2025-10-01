package com.example.tictactoe.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GameDao {
    @Insert
    suspend fun insertGame(game: GameEntity)

    @Query("SELECT * FROM games ORDER BY timestamp DESC")
    suspend fun getAllGames(): List<GameEntity>
}