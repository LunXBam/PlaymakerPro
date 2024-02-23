package com.example.coaching_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.coaching_app.databinding.ActivityEditRosterBinding

class EditRosterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditRosterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_roster)
        binding = ActivityEditRosterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.createPlayerButton.setOnClickListener{
            var firstName = binding.firstNameEditText.text.toString().trim()
            var lastName = binding.lastNameEditText.text.toString().trim()
            var height = binding.heightEditText.text.toString().trim()
            var weight = binding.weightEditText.text.toString().trim()
            var birthdate = binding.birthdateEditText.text.toString().trim()
            var nationality = binding.nationalityEditText.text.toString().trim()
            var jerseyNumber = binding.jerseyNumberEditText.text.toString().trim()
            var position = binding.positionEditText.text.toString().trim()

            if(firstName.isNotEmpty() && lastName.isNotEmpty()
                && jerseyNumber.isNotEmpty() && position.isNotEmpty()){
                var player = PlayerModel(firstName, lastName, height, weight,
                    birthdate, nationality, jerseyNumber, position)
            }
        }

    }
}