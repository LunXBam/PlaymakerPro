package com.example.coaching_app
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.coaching_app.databinding.ActivityCreateStatsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CreateStatsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateStatsBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_stats)
        binding = ActivityCreateStatsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val selectedPlayer = intent.getParcelableExtra<PlayerModel>("selectedPlayer")

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

            if(shots.isNotEmpty() && assists.isNotEmpty() && blocks.isNotEmpty() && blocks.isNotEmpty()
                && fouls.isNotEmpty() && goals.isNotEmpty())
            {
                val db = FirebaseFirestore.getInstance().collection("soccer_stats")

                val statID = db.document().id
                val playerID = selectedPlayer?.playerID
                val teamID = selectedPlayer?.teamID

                val stat = StatsModel(assists,blocks,fouls,gamesPlayed,goals,
                injured,playerID,redCards,shots,statID,tackles,yellowCards,teamID)

                db.document(statID).set(stat)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Stats Saved", Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener{
                        it.localizedMessage?.let { it1 -> Log.w("DB issue", it1)}
                    }

                val intent = Intent(this, PlayerActivity::class.java)
                intent.putExtra("selectedPlayer", selectedPlayer)
                startActivity(intent)
            }

            else
            {
                Toast.makeText(this, "All fields must be filled in!!!",
                    Toast.LENGTH_LONG).show()
            }
        }


        binding.backButton.setOnClickListener {
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra("selectedPlayer", selectedPlayer)
            startActivity(intent)
        }


    }
}