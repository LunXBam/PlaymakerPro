package com.example.coaching_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.coaching_app.databinding.ActivityEditRosterBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore

class EditRosterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditRosterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_roster)
        binding = ActivityEditRosterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

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

                val id = db.document().id

                val player = PlayerModel(firstName, lastName, height, weight,
                    birthdate, nationality, jerseyNumber, position, id)

                db.document(id).set(player)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Roster Successfully Updated",
                            Toast.LENGTH_LONG).show() }
                    .addOnFailureListener{
                        it.localizedMessage?.let { it1 -> Log.w("DB issue", it1) }
                    }

                val intent = Intent(this, RosterActivity::class.java)
                startActivity(intent)
            }
            else{
                Toast.makeText(this, "First/Last name, Jersey Number, and Position must be filled in",
                    Toast.LENGTH_LONG).show()
            }
        }
    }
}