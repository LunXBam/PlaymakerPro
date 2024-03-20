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
        val selectedTeam = intent.getParcelableExtra<Team>("selectedTeam")

        val playerName = selectedPlayer?.firstName + " " + selectedPlayer?.lastName

        val playerID = selectedPlayer?.playerID

        val playerNameInput = findViewById<TextView>(R.id.playerNameInput)
        playerNameInput.text = playerName

        val playerPosition = selectedPlayer?.playerPosition

        val playerPositionInput = findViewById<TextView>(R.id.PositionNameInput)
        playerPositionInput.text = playerPosition

        val playerTeamID = selectedPlayer?.teamID


        val userID = FirebaseAuth.getInstance().currentUser?.uid

        val db_info = FirebaseFirestore.getInstance().collection("teams")

        if (playerTeamID != null) {
            db_info.document(playerTeamID).get()
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


        val db_stat = FirebaseFirestore.getInstance()
        val collectionRef = db_stat.collection("soccer_stats")
        val query = collectionRef.whereEqualTo("playerID",playerID).limit(1)

        query.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val statData = document.toObject(StatsModel::class.java)
                    if (statData != null) {
                        val stat_goals = statData.goals
                        val stat_assists = statData.assists
                        val stat_blocks = statData.blocks
                        val stat_shots = statData.shots
                        val stat_fouls = statData.fouls
                        val stat_tackles = statData.tackles
                        val stat_red_cards = statData.redCards
                        val stat_yellow_cards = statData.yellowCards
                        val stat_games_played = statData.gamesPlayed

                        val goalInput = findViewById<TextView>(R.id.GoalInput)
                        goalInput.text = stat_goals

                        val assistsInput = findViewById<TextView>(R.id.AssistsInput)
                        assistsInput.text = stat_assists

                        val blockInput = findViewById<TextView>(R.id.BlocksInput)
                        blockInput.text = stat_blocks

                        val shotInput = findViewById<TextView>(R.id.ShotsInput)
                        shotInput.text = stat_shots

                        val foulInput = findViewById<TextView>(R.id.FoulInput)
                        foulInput.text = stat_fouls

                        val tackleInput = findViewById<TextView>(R.id.TacklesInput)
                        tackleInput.text = stat_tackles

                        val yellowCardInput = findViewById<TextView>(R.id.yellowCardInput)
                        yellowCardInput.text = stat_yellow_cards

                        val redCardInput = findViewById<TextView>(R.id.redCardInput)
                        redCardInput.text = stat_red_cards

                        val gamesPlayedInput = findViewById<TextView>(R.id.gamesPlayedInput)
                        gamesPlayedInput.text = stat_games_played
                    }

                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting documents: ", exception)
            }


        binding.editStatButton.setOnClickListener {
            val intent = Intent(this, createStatsActivity::class.java)
            intent.putExtra("selectedPlayer", selectedPlayer)
            startActivity(intent)
        }

        binding.backButton.setOnClickListener{
            val intent = Intent(this, RosterActivity::class.java)
            intent.putExtra("selectedTeam",selectedTeam)
            startActivity(intent)
        }

    }

}