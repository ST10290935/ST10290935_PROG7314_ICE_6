package com.example.tictactoe.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val playerX: String,
    val playerO: String,
    val winner: String,
    val timestamp: Long
)