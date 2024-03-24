package com.example.coaching_app

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.coaching_app.databinding.ActivityEditDeleteGameHistBinding
import com.google.firebase.firestore.FirebaseFirestore

class EditDeleteGameHistActivity : AppCompatActivity() {

    private lateinit var binding:ActivityEditDeleteGameHistBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditDeleteGameHistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Grab the selected team from put extra
        val selectedGame = intent.getParcelableExtra<GameHistory>("selectedGame")
        val selectedTeam = intent.getParcelableExtra<Team>("selectedTeam")

        // fill in the input fields based on the
        val opponentText = findViewById<TextView>(R.id.opponentEditText)
           opponentText.text = selectedGame?.opponent

        val oppScore = findViewById<TextView>(R.id.oppScoreEditText)
        oppScore.text = selectedGame?.opponentScore

        val ourScore = findViewById<TextView>(R.id.ourScoreEditText)
        ourScore.text = selectedGame?.ourScore

        val result = findViewById<TextView>(R.id.resultEditText)
        result.text = selectedGame?.result

        val gameDate = findViewById<TextView>(R.id.gameDateEditText)
        gameDate.text = selectedGame?.gameDate

        val venue = findViewById<TextView>(R.id.venueEditText)
        venue.text = selectedGame?.venue

        val weather = findViewById<TextView>(R.id.weatherEditText)
        weather.text = selectedGame?.weather



        // on click listener to edit chosen game history
        binding.editHist.setOnClickListener{

            // grab the values in the edit text
            val newOpp = binding.opponentEditText.text.toString().trim()
            val newOppScore = binding.oppScoreEditText.text.toString().trim()
            val newOurScore = binding.ourScoreEditText.text.toString().trim()
            val newResult = binding.resultEditText.text.toString().trim()
            val newGameDay = binding.gameDateEditText.text.toString().trim()
            val newVenue = binding.venueEditText.text.toString().trim()
            val newWeather = binding.weatherEditText.text.toString().trim()

            // validation
            if(newOpp.isNotEmpty() && newOppScore.isNotEmpty() && newOurScore.isNotEmpty()
                && newResult.isNotEmpty() && newGameDay.isNotEmpty() && newVenue.isNotEmpty()
                && newWeather.isNotEmpty()){

                // connect to the db
                val db = FirebaseFirestore.getInstance().collection("game_history")

                // create new game history
                val newGameHist = GameHistory(selectedGame?.teamID.toString(), newGameDay,newOpp,newResult,newOurScore,
                    newOppScore,newVenue,newWeather, selectedGame?.gameID.toString(), selectedGame?.userID.toString())

                // make edits to the game history with the entered game history ID in the db
                db.document(selectedGame?.gameID!!).set(newGameHist)

                // after edits are made send them to the game history page
                val myIntent = Intent(this,GameHistoryActivity::class.java)
                myIntent.putExtra("selectedTeam", selectedTeam)
                startActivity(myIntent)

            }

            else{
                Toast.makeText(this, "All fields must be filled in!!!",
                    Toast.LENGTH_LONG).show()
            }

        }

        // onclick listener for on delete button
        binding.deleteBtn.setOnClickListener{

            // create a yes/no confirmation to delete the game hist
            val builder = AlertDialog.Builder(this@EditDeleteGameHistActivity)
            builder.setTitle("Confirm Delete")
            builder.setMessage("Are you sure you want to delete this game history? It will not come back!!!")

            // If they confirm they want to delete, delete the game history
            builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, i ->
                // connect to the db
                val db = FirebaseFirestore.getInstance().collection("game_history")

                // delete game history
                db.document(selectedGame?.gameID!!).delete()

                dialogInterface.cancel()

                val myIntent = Intent(this, GameHistoryActivity::class.java)
                myIntent.putExtra("selectedTeam", selectedTeam)
                startActivity(myIntent)
            })

            // if they confirm they don't want to delete
            builder.setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.cancel()
            })

            val alert = builder.create()
            alert.show()
        }
    }
}