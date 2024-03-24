package com.example.coaching_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coaching_app.databinding.ActivityLandingPageBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

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

        val bundle: Bundle? = intent.extras
        val selectedTeam = intent.getParcelableExtra<Team>("selectedTeam")

        val title = binding.nameLandingTextView
        title.text = selectedTeam?.teamName

        recyclerView = findViewById(R.id.gameLandingRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        // connect to the db and then only show the last 3 games of the team

//        val viewModel: GameHistViewModel by viewModels()
//        viewModel.getGameHistory().observe(this) { gameHist ->
//            recyclerView.adapter = GameHistoryRecyclerViewAdapter(gameHist, selectedTeam)
//        }

        val db = FirebaseFirestore.getInstance().collection("soccer_stats")
        db.whereEqualTo("teamID", selectedTeam?.teamID)
            .orderBy("goals")
            .limitToLast(1)
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

        /*
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

    }
}