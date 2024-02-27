package com.example.coaching_app

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PlaybookViewModel : ViewModel() {
    private val playbookData = MutableLiveData<List<PlaybookModel>>()

    init {

        val userID = FirebaseAuth.getInstance().currentUser?.uid

        val db = FirebaseFirestore.getInstance().collection("play_book")
            .whereEqualTo("userID", userID)
            .addSnapshotListener{ documents,exception ->
                if(exception != null){
                    Log.w("Successful", "Listen Failed ${exception.code}")
                    return@addSnapshotListener
                }
                documents?.let {
                    val playbookList = ArrayList<PlaybookModel>()
                    for(document in documents){
                        Log.i("Successful", "${document.data}")
                        val play = document.toObject(PlaybookModel::class.java)
                        playbookList.add(play)
                    }
                    playbookData.value = playbookList
                }
            }
    }

    // function to return the playbook data
    fun getPlayBook() : LiveData<List<PlaybookModel>> {
        return playbookData
    }
}