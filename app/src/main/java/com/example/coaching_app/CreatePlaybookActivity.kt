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
import com.example.coaching_app.databinding.ActivityCreatePlaybookBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.ByteArrayOutputStream


class CreatePlaybookActivity : DrawerBaseActivity() {

    private lateinit var binding: ActivityCreatePlaybookBinding

    private lateinit var imageView: ImageView
    private lateinit var uploadButton: Button
    private var imageString: String? = null
    private val PICK_IMAGE_REQUEST = 1



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_playbook)
        binding = ActivityCreatePlaybookBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        allocateActivityTitle("Add Playbook")

        imageView = findViewById(R.id.imageView)
        uploadButton = findViewById(R.id.uploadPlayButton)

        uploadButton.setOnClickListener {
            openGallery()
        }

        val bundle: Bundle? = intent.extras
        val selectedTeam = intent.getParcelableExtra<Team>("selectedTeam")



        binding.createPlayButton.setOnClickListener{
            val playName = binding.playNameEditText.text.toString().trim()
            val playDescription = binding.playDescriptionEditText.text.toString().trim()
            if(playName.isNotEmpty() && playDescription.isNotEmpty()){

                val db = FirebaseFirestore.getInstance().collection("play_book")

                val playID = db.document().id
                val teamID = selectedTeam?.teamID
                val userID = FirebaseAuth.getInstance().currentUser?.uid
                val playPhoto = imageString

                val play = PlaybookModel(playPhoto,
                    playName, playDescription, playID, teamID, userID)

                db.document(playID).set(play)
                    .addOnSuccessListener {
                        Toast.makeText(this, "New Play Uploaded Successfully",
                            Toast.LENGTH_LONG).show() }
                    .addOnFailureListener{
                        it.localizedMessage?.let { it1 -> Log.w("DB issue", it1) }
                    }

                val intent = Intent(this, PlaybookActivity::class.java)
                intent.putExtra("selectedTeam",selectedTeam)
                startActivity(intent)
            }
            else{
                Toast.makeText(this, "Play Name and Description must be filled in",
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
}
