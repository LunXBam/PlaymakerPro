package com.example.coaching_app

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TeamSelectViewModel : ViewModel(){
    private val teamsData = MutableLiveData<List<Team>>()

    init {
        val userID = FirebaseAuth.getInstance().currentUser?.uid

        val db = FirebaseFirestore.getInstance().collection("teams")
            .whereEqualTo("coachID", userID)
            .addSnapshotListener{ documents,exception ->
                if(exception != null){
                    Log.w("Successful", "Listen Failed ${exception.code}")
                    return@addSnapshotListener
                }
                documents?.let {
                    val teamList = ArrayList<Team>()
                    for(document in documents){
                        Log.i("Successful", "${document.data}")
                        val team = document.toObject(Team::class.java)
                        teamList.add(team)
                    }
                    teamsData.value = teamList
                }
            }
    }

    // function to return the team data
    fun getTeams() : LiveData<List<Team>> {
        return teamsData
    }
}