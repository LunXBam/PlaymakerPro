package com.example.coaching_app


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Toast
import com.example.coaching_app.databinding.ActivityEditRosterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.ByteArrayOutputStream

class EditRosterActivity : DrawerBaseActivity() {

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
        allocateActivityTitle("Edit Roster")

        imageView = findViewById(R.id.imageView)
        uploadButton = findViewById(R.id.uploadPlayerPhotoButton)

        uploadButton.setOnClickListener {
            openGallery()
        }

        //supportActionBar?.title = "Edit Roster"
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.heightSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                val feet = (progress / 12).toString()
                val inches = (progress % 12).toString()
                val height = "Height: " + feet + "' " + inches + " \""
                binding.heightValue.text = height
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })

        binding.weightSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                val weight = "Weight: " + progress.toString() + "lbs"
                binding.weightValue.text = weight
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })

        val soccerPositions = arrayOf("Goal Keeper", "Right Fullback", "Left Fullback", "Center Back", "Center Midfield",
            "Right Midfield / Wing", "Left Midfield / Wing", "Attacking Center Mid", "Center Forward", "Defensive Center Mid",
            "Left Center Back", "Right Center Back")
        val positionsAdapter = ArrayAdapter(this@EditRosterActivity,android.R.layout.simple_spinner_dropdown_item, soccerPositions)

        binding.positionSpinner.adapter = positionsAdapter


        val bundle: Bundle? = intent.extras
        val selectedTeam = intent.getParcelableExtra<Team>("selectedTeam")
        val selectedPlayer = intent.getParcelableExtra<PlayerModel>("selectedPlayer")

        if(selectedPlayer?.playerID?.isNotEmpty() == true){
            binding.firstNameEditText.setText(selectedPlayer.firstName)
            binding.lastNameEditText.setText(selectedPlayer.lastName)
            val feet = (selectedPlayer.height?.toInt()!! / 12).toString()
            val inches = (selectedPlayer.height?.toInt()!! % 12).toString()
            val heightString = "Height: " + feet + "' " + inches + " \""
            binding.heightValue.text = heightString
            binding.heightSeekBar.progress = selectedPlayer.height?.toInt()!!
            val weightString = "Weight: " + selectedPlayer.weight.toString() + "lbs"
            binding.weightValue.text = weightString
            binding.weightSeekBar.progress = selectedPlayer.weight?.toInt()!!
            binding.birthdateEditText.setText(selectedPlayer.birthdate)
            binding.nationalityEditText.setText(selectedPlayer.nationality)
            binding.jerseyNumberEditText.setText(selectedPlayer.jerseyNumber)
            binding.positionSpinner.setSelection(0)
            for((index, position) in soccerPositions.withIndex()){
                if(selectedPlayer.playerPosition == position){
                    binding.positionSpinner.setSelection(index)
                }
            }
            imageString = selectedPlayer.playerPhoto
            val bitmap = base64ToBitmap(imageString)
            if (bitmap != null)
            {
                imageView.setImageBitmap(bitmap)
            }
        }

        binding.createPlayerButton.setOnClickListener{
            val firstName = binding.firstNameEditText.text.toString().trim()
            val lastName = binding.lastNameEditText.text.toString().trim()
            val height = binding.heightSeekBar.progress.toString().trim()
            val weight = binding.weightSeekBar.progress.toString().trim()
            val birthdate = binding.birthdateEditText.text.toString().trim()
            val nationality = binding.nationalityEditText.text.toString().trim()
            val jerseyNumber = binding.jerseyNumberEditText.text.toString().trim()
            val position = binding.positionSpinner.selectedItem.toString().trim()
            val playerPhoto = imageString

            if(firstName.isNotEmpty() && lastName.isNotEmpty()
                && jerseyNumber.isNotEmpty() && position.isNotEmpty()){

                val db = FirebaseFirestore.getInstance().collection("players")

                val userID = FirebaseAuth.getInstance().currentUser?.uid
                val teamID = selectedTeam?.teamID
                var playerID = ""

                if(selectedPlayer?.playerID?.isNotEmpty() == true){
                    playerID = selectedPlayer.playerID!!
                }
                else{
                    playerID = db.document().id
                }

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
            var bitmap = BitmapFactory.decodeStream(inputStream)
            bitmap = resizeBitmap(bitmap, 300, 300)
            imageView.setImageBitmap(bitmap)
            val base64String = bitmapToBase64(bitmap)
            imageString = base64String
        }
    }

    fun resizeBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        var width = bitmap.width
        var height = bitmap.height

        if (width > maxWidth || height > maxHeight) {
            val aspectRatio = width.toFloat() / height.toFloat()
            if (width > height) {
                width = maxWidth
                height = (width / aspectRatio).toInt()
            } else {
                height = maxHeight
                width = (height * aspectRatio).toInt()
            }
        }

        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }


    fun bitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream) // Initially compress with maximum quality
        var quality = 100 // Initial quality
        val maxSizeBytes = 50 * 1024 // 50 KB in bytes

        // Check if the size exceeds the maximum allowed size
        while (outputStream.size() > maxSizeBytes && quality > 0) {
            outputStream.reset() // Clear the output stream
            quality -= 10 // Reduce quality by 10 (you can adjust this increment)
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream) // Compress with reduced quality
        }

        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun base64ToBitmap(base64String: String?): Bitmap? {
        if (base64String.isNullOrEmpty()) {
            return null
        }
        return try {
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: IllegalArgumentException) {
            // Log the error or handle it as needed
            null
        }
    }
}