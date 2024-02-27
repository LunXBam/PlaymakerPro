package com.example.coaching_app


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class GameHistViewModel () : ViewModel() {
    private val gameHistories = MutableLiveData<List<GameHistory>>()

    init {

        val userID = FirebaseAuth.getInstance().currentUser?.uid

        val db = FirebaseFirestore.getInstance().collection("game_history")
            .addSnapshotListener{ documents,exception ->
                if(exception != null){
                    Log.w("Successful", "Listen Failed ${exception.code}")
                    return@addSnapshotListener
                }
                documents?.let {
                    val gameHistList = ArrayList<GameHistory>()
                    for(document in documents){
                        Log.i("Successful", "${document.data}")
                        val gameHist = document.toObject(GameHistory::class.java)
                        gameHistList.add(gameHist)
                    }
                    gameHistories.value = gameHistList
                }
            }
    }

    // function to return the game history data
    fun getGameHistory() : LiveData<List<GameHistory>>{
        return gameHistories
    }

//    fun getLimitedHistory(selectedTeam : Team?) : LiveData<List<GameHistory>>{
//        return getGameHistory().map { games ->
//            games
//                .filter { game ->
//                    game.teamID == selectedTeam?.teamID
//                }
//        }
//    }
}