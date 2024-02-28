package com.example.coaching_app

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RosterViewModel : ViewModel() {
    private val playersData = MutableLiveData<List<PlayerModel>>()

    init {

        val userID = FirebaseAuth.getInstance().currentUser?.uid
        val recievedData = DataRepo.sharedData

        val db = FirebaseFirestore.getInstance().collection("players")
            .whereEqualTo("teamID", recievedData?.teamID)
            .addSnapshotListener{ documents,exception ->
                if(exception != null){
                    Log.w("Successful", "Listen Failed ${exception.code}")
                    return@addSnapshotListener
                }
                documents?.let {
                    val playerList = ArrayList<PlayerModel>()
                    for(document in documents){
                        Log.i("Successful", "${document.data}")
                        val player = document.toObject(PlayerModel::class.java)
                        playerList.add(player)
                    }
                    playersData.value = playerList
                }
            }
    }

    // function to return the player data
    fun getRoster() : LiveData<List<PlayerModel>> {
        return playersData
    }
}