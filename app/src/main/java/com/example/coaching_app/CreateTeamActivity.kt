package com.example.coaching_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.coaching_app.databinding.ActivityCreateTeamBinding
import com.example.coaching_app.databinding.ActivityEditRosterBinding
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

        binding.createPlayerButton.setOnClickListener{
            val teamName = binding.teamNameEditText.text.toString().trim()
            val sport = binding.sportEditText.text.toString().trim()
            val city = binding.cityEditText.text.toString().trim()
            val colors = binding.colorsEditText.text.toString().trim()

            if(teamName.isNotEmpty() && sport.isNotEmpty()){

                val db = FirebaseFirestore.getInstance().collection("teams")

                val teamID = db.document().id

                val team = Team(teamName, sport, city, colors, null, null, teamID)

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