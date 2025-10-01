package com.example.tictactoe

import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var buttons: Array<Button>
    private lateinit var playerXText: TextView
    private lateinit var playerOText: TextView
    private lateinit var winnerText: TextView
    private lateinit var resetButton: Button
    private lateinit var modeSwitch: Switch

    private var board = Array(9) { "" }
    private var currentPlayer = "X"
    private var gameActive = true
    private var scoreX = 0
    private var scoreO = 0

    private var isOnline = true
    private val firestore = FirebaseFirestore.getInstance()
    private val gameId = "tictactoe_results" // Firestore document for storing results

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI
        playerXText = findViewById(R.id.playerXText)
        playerOText = findViewById(R.id.playerOText)
        winnerText = findViewById(R.id.winnerText)
        resetButton = findViewById(R.id.resetButton)
        modeSwitch = findViewById(R.id.modeSwitch)
        buttons = Array(9) { i ->
            findViewById(resources.getIdentifier("btn$i", "id", packageName))
        }

        // Set button clicks
        buttons.forEachIndexed { index, button ->
            button.setOnClickListener { onCellClicked(index) }
        }

        // Reset game
        resetButton.setOnClickListener { resetGame() }

        // Toggle mode
        modeSwitch.isChecked = isOnline
        modeSwitch.setOnCheckedChangeListener { _, isChecked ->
            isOnline = isChecked
            resetGame()
        }

        updateScores()
    }

    private fun onCellClicked(index: Int) {
        if (!gameActive || board[index].isNotEmpty()) return

        board[index] = currentPlayer
        buttons[index].text = currentPlayer

        if (checkWin()) {
            gameActive = false
            if (currentPlayer == "X") scoreX++ else scoreO++
            winnerText.text = "Player $currentPlayer Wins!"
            updateScores()
            if (isOnline) saveResultOnline("Player $currentPlayer Wins")
        } else if (board.all { it.isNotEmpty() }) {
            gameActive = false
            winnerText.text = "It's a Draw!"
            if (isOnline) saveResultOnline("Draw")
        } else {
            currentPlayer = if (currentPlayer == "X") "O" else "X"
        }
    }

    private fun checkWin(): Boolean {
        val winPositions = arrayOf(
            intArrayOf(0, 1, 2),
            intArrayOf(3, 4, 5),
            intArrayOf(6, 7, 8),
            intArrayOf(0, 3, 6),
            intArrayOf(1, 4, 7),
            intArrayOf(2, 5, 8),
            intArrayOf(0, 4, 8),
            intArrayOf(2, 4, 6)
        )
        return winPositions.any { (a, b, c) ->
            board[a].isNotEmpty() && board[a] == board[b] && board[a] == board[c]
        }
    }

    private fun resetGame() {
        board.fill("")
        buttons.forEach { it.text = "" }
        currentPlayer = "X"
        gameActive = true
        winnerText.text = ""
    }

    private fun updateScores() {
        playerXText.text = "Player X: $scoreX"
        playerOText.text = "Player O: $scoreO"
    }

    private fun saveResultOnline(result: String) {
        val gameDoc = firestore.collection("tictactoe").document(gameId)
        gameDoc.set(
            mapOf(
                "lastResult" to result,
                "scoreX" to scoreX,
                "scoreO" to scoreO,
                "timestamp" to System.currentTimeMillis()
            )
        )
    }
}
