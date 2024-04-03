package com.example.coaching_app

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coaching_app.databinding.ActivityLandingPageBinding
import com.google.firebase.firestore.FirebaseFirestore

class LandingPageActivity : DrawerBaseActivity() {

    private lateinit var binding: ActivityLandingPageBinding
    private lateinit var recyclerView: RecyclerView

    @SuppressWarnings("deprecation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)
        binding = ActivityLandingPageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        allocateActivityTitle("Home")

        val bundle: Bundle? = intent.extras
        val selectedTeam = intent.getParcelableExtra<Team>("selectedTeam")

        val teamLogoString = selectedTeam?.logo
        val imageView = findViewById<ImageView>(R.id.logoLandingImageView)
        val bitmap = base64ToBitmap(teamLogoString)
        if (bitmap != null)
        {
            imageView.setImageBitmap(bitmap)
        } else
        {
            imageView.setImageResource(R.drawable.default_team_logo)
        }

        val title = binding.nameLandingTextView
        title.text = selectedTeam?.teamName
        val city = binding.cityNameTextView
        city.text = selectedTeam?.city
        val sport = binding.sportNameTextView
        sport.text = selectedTeam?.sport

        recyclerView = findViewById(R.id.gameLandingRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)



        val viewModel: GameHistViewModel by viewModels()
        viewModel.getGameHistory().observe(this) { gameHist ->
            recyclerView.adapter = GameHistoryRecyclerViewAdapter(gameHist, selectedTeam)
        }

        val db2 = FirebaseFirestore.getInstance().collection("players")
        db2.whereEqualTo("teamID", selectedTeam?.teamID)
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->
                for(document in documents){
                    val playerData = document.toObject(PlayerModel::class.java)
                    val playerName = "Name: " + playerData?.firstName?.substring(0, 1) +
                            ". " + playerData?.lastName
                    val playerNumber = "Jersey: #" + playerData?.jerseyNumber
                    binding.playerNameLandingTextView.text = playerName
                    binding.playerNumberLandingTextView.text = playerNumber
                    val db = FirebaseFirestore.getInstance().collection("soccer_stats")
                    db.whereEqualTo("playerID", playerData.playerID)
                        .get()
                        .addOnSuccessListener { documents ->
                            for (document in documents){
                                val playerStats = document.toObject(StatsModel::class.java)
                                val goals = "Goals: " + playerStats.goals
                                val assists = "Assists: " + playerStats.assists

                                binding.playerGoalsLandingTextView.text = goals
                                binding.playerAssistsLandingTextView.text = assists
                            }
                        }
                }
            }
/*
        val db = FirebaseFirestore.getInstance().collection("soccer_stats")
        db.whereEqualTo("teamID", selectedTeam?.teamID)
            .orderBy("goals")
            .limit(1)
            .get()
            .addOnSuccessListener { querySnapShot ->
                for (document in querySnapShot) {
                    val playerStats = document.toObject(StatsModel::class.java)
                    binding.playerGoalsLandingTextView.text = playerStats.goals
                    binding.playerAssistsLandingTextView.text = playerStats.assists
                    val bestPlayerID = playerStats.playerID

                    val db2 = FirebaseFirestore.getInstance().collection("players")
                    if (bestPlayerID != null) {
                        db2.document(bestPlayerID).get()
                            .addOnSuccessListener { documentSnapshot ->
                                if (documentSnapshot.exists()) {
                                    val playerData =
                                        documentSnapshot.toObject(PlayerModel::class.java)
                                    val playerName = playerData?.firstName?.substring(
                                        0,
                                        1
                                    ) + ". " + playerData?.lastName
                                    val playerNumber = "#" + playerData?.jerseyNumber
                                    binding.playerNameLandingTextView.text = playerName
                                    binding.playerNumberLandingTextView.text = playerNumber
                                } else {
                                    // Document does not exist
                                    Log.d("Firestore", "No such document with ID: $bestPlayerID")
                                }
                            }
                    }
                }
            }



                val db = FirebaseFirestore.getInstance().collection("players")
                db.whereEqualTo("teamID", selectedTeam?.teamID)
                    .get()
                    .addOnSuccessListener { querySnapShot ->
                        for (document in querySnapShot) {
                            val player = document.toObject(PlayerModel::class.java)
                            val db2 = FirebaseFirestore.getInstance().collection("soccer_stats")
                            db.whereEqualTo("playerID", player.playerID)
                                .orderBy("goals")
                                .limitToLast(1)
                                .get()
                                .addOnSuccessListener { querySnapShot ->
                                    for (document in querySnapShot) {

                                    }
                                }
                        }
                    }
                */


        binding.landingToRosterButton.setOnClickListener{
            val intent = Intent(this, RosterActivity::class.java)
            intent.putExtra("selectedTeam",selectedTeam)
            startActivity(intent)
        }

        binding.landingToGamesButton.setOnClickListener{
            val intent = Intent(this, GameHistoryActivity::class.java)
            intent.putExtra("selectedTeam",selectedTeam)
            startActivity(intent)
        }

        binding.landingToPlaysButton.setOnClickListener{
            val intent = Intent(this, PlaybookActivity::class.java)
            intent.putExtra("selectedTeam",selectedTeam)
            startActivity(intent)
        }

        binding.landingToSchedulerButton.setOnClickListener{
            val intent = Intent(this, SchedulerActivity::class.java)
            intent.putExtra("selectedTeam",selectedTeam)
            startActivity(intent)
        }

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