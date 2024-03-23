package com.example.coaching_app


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.coaching_app.databinding.ActivityEditRosterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.ByteArrayOutputStream

class EditRosterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditRosterBinding
    private lateinit var imageView: ImageView
    private lateinit var uploadButton: Button
    private var imageString: String? = null

    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_roster)
        binding = ActivityEditRosterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        imageView = findViewById(R.id.imageView)
        uploadButton = findViewById(R.id.uploadPlayerPhotoButton)

        uploadButton.setOnClickListener {
            openGallery()
        }

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
            val playerPhoto = imageString

            if(firstName.isNotEmpty() && lastName.isNotEmpty()
                && jerseyNumber.isNotEmpty() && position.isNotEmpty()){


                val db = FirebaseFirestore.getInstance().collection("players")
                val userID = FirebaseAuth.getInstance().currentUser?.uid
                val teamID = selectedTeam?.teamID

                val playerID = db.document().id

                val player = PlayerModel(firstName, lastName, height, weight,
                    birthdate, nationality, jerseyNumber, position, playerID, teamID, userID, playerPhoto)

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

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null )
        {
            val selectedImageUri = data.data
            val inputStream = contentResolver.openInputStream(selectedImageUri!!)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            imageView.setImageBitmap(bitmap)

            val base64String = bitmapToBase64(bitmap)

            imageString = base64String

            println("Base64 String: $base64String")
        }
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}