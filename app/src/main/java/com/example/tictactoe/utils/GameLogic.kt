package com.example.tictactoe.utils

class GameLogic {
    fun checkWinner(board: List<String>): String {
        val lines = listOf(
            listOf(0,1,2), listOf(3,4,5), listOf(6,7,8),
            listOf(0,3,6), listOf(1,4,7), listOf(2,5,8),
            listOf(0,4,8), listOf(2,4,6)
        )
        for (line in lines) {
            val (a,b,c) = line
            if (board[a].isNotEmpty() && board[a] == board[b] && board[a] == board[c])
                return board[a]
        }
        return if (board.all { it.isNotEmpty() }) "Draw" else ""
    }
}
