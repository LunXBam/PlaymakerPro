package com.example.coaching_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.coaching_app.databinding.ActivityCreateGameHistoryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class CreateGameHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateGameHistoryBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_game_history)
        binding = ActivityCreateGameHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle: Bundle? = intent.extras
        val selectedTeam = intent.getParcelableExtra<Team>("selectedTeam")

        binding.createGameHist.setOnClickListener{
            val oppName = binding.opponentEditText.text.toString().trim()
            val oppScore = binding.oppScoreEditText.text.toString().trim()
            val ourScore = binding.ourScoreEditText.text.toString().trim()
            val result = binding.resultEditText.text.toString().trim()
            val gameDate = binding.gameDateEditText.text.toString().trim()
            val venue = binding.venueEditText.text.toString().trim()
            val weather = binding.weatherEditText.text.toString().trim()

            if(oppName.isNotEmpty() && oppScore.isNotEmpty() && ourScore.isNotEmpty()
                && result.isNotEmpty() && gameDate.isNotEmpty() && venue.isNotEmpty() && weather.isNotEmpty()){

                // initalized db
                val db = FirebaseFirestore.getInstance().collection("game_history")

                val gameID = db.document().id
                val userID = FirebaseAuth.getInstance().currentUser?.uid
                val teamID = selectedTeam?.teamID

                // create the new game history
                val gameHist = GameHistory(teamID,gameDate,oppName,result,ourScore,oppScore,venue,weather,gameID,userID )

                db.document(gameID).set(gameHist)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Game History Successfully Created",
                            Toast.LENGTH_LONG).show() }
                    .addOnFailureListener{
                        it.localizedMessage?.let { it1 -> Log.w("DB issue", it1) }
                    }

                val intent = Intent(this, GameHistoryActivity::class.java)
                intent.putExtra("selectedTeam",selectedTeam)
                startActivity(intent)
            }

            else{
                Toast.makeText(this, "All fields must be filled in!!!!",
                    Toast.LENGTH_LONG).show()
            }
        }

        binding.goBack.setOnClickListener{
            val intent = Intent(this, GameHistoryActivity::class.java)
            intent.putExtra("selectedTeam",selectedTeam)
            startActivity(intent)
        }
    }
}
