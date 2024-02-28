package com.example.coaching_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.coaching_app.databinding.ActivityEditRosterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditRosterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditRosterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_roster)
        binding = ActivityEditRosterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //supportActionBar?.title = "Edit Roster"
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val bundle: Bundle? = intent.extras
        val selectedTeam = intent.getParcelableExtra<Team>("selectedTeam")

        binding.createPlayerButton.setOnClickListener{
            val firstName = binding.firstNameEditText.text.toString().trim()
            val lastName = binding.lastNameEditText.text.toString().trim()
            val height = binding.heightEditText.text.toString().trim()
            val weight = binding.weightEditText.text.toString().trim()
            val birthdate = binding.birthdateEditText.text.toString().trim()
            val nationality = binding.nationalityEditText.text.toString().trim()
            val jerseyNumber = binding.jerseyNumberEditText.text.toString().trim()
            val position = binding.positionEditText.text.toString().trim()

            if(firstName.isNotEmpty() && lastName.isNotEmpty()
                && jerseyNumber.isNotEmpty() && position.isNotEmpty()){


                val db = FirebaseFirestore.getInstance().collection("players")
                val userID = FirebaseAuth.getInstance().currentUser?.uid
                val teamID = selectedTeam?.teamID

                val playerID = db.document().id

                val player = PlayerModel(firstName, lastName, height, weight,
                    birthdate, nationality, jerseyNumber, position, playerID, teamID, userID)

                db.document(playerID).set(player)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Roster Successfully Updated",
                            Toast.LENGTH_LONG).show() }
                    .addOnFailureListener{
                        it.localizedMessage?.let { it1 -> Log.w("DB issue", it1) }
                    }

                val intent = Intent(this, RosterActivity::class.java)
                intent.putExtra("selectedTeam",selectedTeam)
                startActivity(intent)
            }
            else{
                Toast.makeText(this, "First/Last name, Jersey Number, and Position must be filled in",
                    Toast.LENGTH_LONG).show()
            }
        }
    }
}