package com.example.coaching_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.coaching_app.databinding.ActivityCreateTeamBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CreateTeamActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateTeamBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_team)
        binding = ActivityCreateTeamBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //supportActionBar?.title = "Edit Roster"
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.createTeamButton.setOnClickListener{
            val teamName = binding.teamNameEditText.text.toString().trim()
            val sport = binding.sportEditText.text.toString().trim()
            val city = binding.cityEditText.text.toString().trim()
            val colors = binding.colorsEditText.text.toString().trim()

            if(teamName.isNotEmpty() && sport.isNotEmpty()){

                val db = FirebaseFirestore.getInstance().collection("teams")

                val teamID = db.document().id
                val userID = FirebaseAuth.getInstance().currentUser?.uid

                val team = Team(teamName, sport, city, colors,
                    "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",
                    userID, teamID)

                db.document(teamID).set(team)
                    .addOnSuccessListener {
                        Toast.makeText(this, "New Team Added Successfully",
                            Toast.LENGTH_LONG).show() }
                    .addOnFailureListener{
                        it.localizedMessage?.let { it1 -> Log.w("DB issue", it1) }
                    }

                val intent = Intent(this, TeamSelectActivity::class.java)
                startActivity(intent)
            }
            else{
                Toast.makeText(this, "Team Name and Sport must be filled in",
                    Toast.LENGTH_LONG).show()
            }
        }
    }
}