package com.example.coaching_app
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.coaching_app.databinding.ActivityCreateStatsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


class CreateStatsActivity : DrawerBaseActivity() {


    private lateinit var binding: ActivityCreateStatsBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_stats)
        binding = ActivityCreateStatsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        allocateActivityTitle("Edit Stats")


        val selectedPlayer = intent.getParcelableExtra<PlayerModel>("selectedPlayer")
        val selectedTeam = intent.getParcelableExtra<Team>("selectedTeam")
        val statDataIntent = intent.getParcelableExtra<StatsModel>("statDataIntent")

        if (statDataIntent != null) {
            binding.AssistsText.setText(statDataIntent.assists)
            binding.shotsText.setText(statDataIntent.shots)
            binding.blockText.setText(statDataIntent.blocks)
            binding.foulsText.setText(statDataIntent.fouls)
            binding.goalsText.setText(statDataIntent.goals)
            binding.gamesPlayedText.setText(statDataIntent.gamesPlayed)
            binding.tacklesText.setText(statDataIntent.tackles)
            binding.redCardText.setText(statDataIntent.redCards)
            binding.yellowCardText.setText(statDataIntent.yellowCards)
        }

        binding.createStats.setOnClickListener {

            val shots = binding.shotsText.text.toString().trim()
            val assists = binding.AssistsText.text.toString().trim()
            val blocks = binding.blockText.text.toString().trim()
            val fouls = binding.foulsText.text.toString().trim()
            val goals = binding.goalsText.text.toString().trim()
            val gamesPlayed = binding.gamesPlayedText.text.toString().trim()
            val tackles = binding.tacklesText.text.toString().trim()
            val redCards = binding.redCardText.text.toString().trim()
            val yellowCards = binding.yellowCardText.text.toString().trim()
            //val injured = binding.InjuredText.text.toString().trim()
            val injured = binding.injuredCheckBox.isActivated.toString()

            if (shots.isNotEmpty() && assists.isNotEmpty() && blocks.isNotEmpty() &&
                fouls.isNotEmpty() && goals.isNotEmpty() && gamesPlayed.isNotEmpty() &&
                tackles.isNotEmpty() && redCards.isNotEmpty() && yellowCards.isNotEmpty()
            ) {

                val db = FirebaseFirestore.getInstance()
                val statsCollection = db.collection("soccer_stats")

                // Query to find existing stats document
                val query = statsCollection.whereEqualTo("playerID", selectedPlayer?.playerID)
                    .whereEqualTo("teamID", selectedPlayer?.teamID)

                query.get().addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        // If a document exists, update its data
                        val existingDoc = querySnapshot.documents[0]
                        val stat = StatsModel(
                            assists, blocks, fouls, gamesPlayed, goals,
                            injured, selectedPlayer?.playerID, redCards, shots, existingDoc.id,
                            tackles, yellowCards, selectedPlayer?.teamID
                        )
                        statsCollection.document(existingDoc.id).set(stat, SetOptions.merge())
                            .addOnSuccessListener {
                                Toast.makeText(this, "Stats Updated", Toast.LENGTH_LONG).show()
                                val intent = Intent(this, PlayerActivity::class.java)
                                intent.putExtra("selectedPlayer", selectedPlayer)
                                intent.putExtra("selectedTeam",selectedTeam)
                                startActivity(intent)
                            }
                            .addOnFailureListener { exception ->
                                Log.e("DB Issue", "Error updating stats: ", exception)
                                val intent = Intent(this, PlayerActivity::class.java)
                                intent.putExtra("selectedPlayer", selectedPlayer)
                                intent.putExtra("selectedTeam",selectedTeam)
                                startActivity(intent)
                            }
                    } else {
                        // If no document exists, create a new one
                        val stat = StatsModel(
                            assists, blocks, fouls, gamesPlayed, goals,
                            injured, selectedPlayer?.playerID, redCards, shots, "", tackles,
                            yellowCards, selectedPlayer?.teamID
                        )
                        statsCollection.add(stat)
                            .addOnSuccessListener { documentReference ->
                                val newStatID = documentReference.id
                                val updatedStat = StatsModel(
                                assists, blocks, fouls, gamesPlayed, goals,
                                injured, selectedPlayer?.playerID, redCards, shots, newStatID, tackles,
                                yellowCards, selectedPlayer?.teamID
                            )
                                documentReference.set(updatedStat)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "New Stats Saved", Toast.LENGTH_LONG).show()
                                        val intent = Intent(this, PlayerActivity::class.java)
                                        intent.putExtra("selectedPlayer", selectedPlayer)
                                        intent.putExtra("selectedTeam",selectedTeam)
                                        startActivity(intent)
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.e("DB Issue", "Error saving new stats: ", exception)
                                        val intent = Intent(this, PlayerActivity::class.java)
                                        intent.putExtra("selectedPlayer", selectedPlayer)
                                        intent.putExtra("selectedTeam",selectedTeam)
                                        startActivity(intent)
                                    }
                            }
                            .addOnFailureListener { exception ->
                                Log.e("DB Issue", "Error adding new stats document: ", exception)
                                val intent = Intent(this, PlayerActivity::class.java)
                                intent.putExtra("selectedPlayer", selectedPlayer)
                                intent.putExtra("selectedTeam",selectedTeam)
                                startActivity(intent)
                            }
                    }
                }
            }
            else {
                Toast.makeText(this, "All fields must be filled in!!!", Toast.LENGTH_LONG)
                    .show()
            }
        }

//        binding.backButton.setOnClickListener {
//            val intent = Intent(this, PlayerActivity::class.java)
//            intent.putExtra("selectedPlayer", selectedPlayer)
//            intent.putExtra("selectedTeam",selectedTeam)
//            startActivity(intent)
//        }
    }
}