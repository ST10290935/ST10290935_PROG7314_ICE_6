package com.example.tictactoe.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

data class OnlineGame(
    val board: List<String> = List(9) { "" },
    val currentPlayer: String = "X",
    val winner: String = ""
)

class FirestoreRepository {
    private val db = FirebaseFirestore.getInstance()
    private val gamesCollection = db.collection("online_games")

    fun createGame(gameId: String, callback: (Boolean) -> Unit) {
        val game = OnlineGame()
        gamesCollection.document(gameId).set(game)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    fun updateGame(gameId: String, game: OnlineGame) {
        gamesCollection.document(gameId).set(game)
    }

    fun listenToGame(gameId: String, onUpdate: (OnlineGame) -> Unit): ListenerRegistration {
        return gamesCollection.document(gameId)
            .addSnapshotListener { snapshot, _ ->
                snapshot?.toObject(OnlineGame::class.java)?.let { onUpdate(it) }
            }
    }
}
