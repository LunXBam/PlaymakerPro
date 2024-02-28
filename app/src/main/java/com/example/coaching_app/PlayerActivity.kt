package com.example.coaching_app
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.coaching_app.databinding.PlayerBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PlayerActivity : DrawerBaseActivity()
{
    private lateinit var binding: PlayerBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player)
        binding = PlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val selectedPlayer = intent.getParcelableExtra<PlayerModel>("selectedPlayer")

        val playerName = selectedPlayer?.firstName + " " + selectedPlayer?.lastName

        val playerNameInput = findViewById<TextView>(R.id.playerNameInput)
        playerNameInput.text = playerName

        val playerPosition = selectedPlayer?.playerPosition

        val playerTeamID = selectedPlayer?.teamID


        val userID = FirebaseAuth.getInstance().currentUser?.uid

        val db = FirebaseFirestore.getInstance().collection("teams")

        if (playerTeamID != null) {
            db.document(playerTeamID).get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val teamData = documentSnapshot.toObject(Team::class.java)
                        if (teamData != null) {
                            val teamName = teamData.teamName
                            val teamNameInput = findViewById<TextView>(R.id.TeamNameInput)
                            teamNameInput.text = teamName
                        }
                    } else {
                        // Document does not exist
                        Log.d("Firestore", "No such document with ID: $playerTeamID")
                    }
                }
        }

        binding.editStatButton.setOnClickListener {

        }

        binding.backButton.setOnClickListener{
            val intent = Intent(this, RosterActivity::class.java)
            startActivity(intent)
        }

    }

}