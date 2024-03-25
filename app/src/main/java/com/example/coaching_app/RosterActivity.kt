package com.example.coaching_app

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coaching_app.databinding.ActivityRosterBinding
import com.google.firebase.firestore.FirebaseFirestore

class RosterActivity : DrawerBaseActivity() {
    private lateinit var binding: ActivityRosterBinding
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_roster)
        binding = ActivityRosterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //supportActionBar?.title = "Team Roster"
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val bundle: Bundle? = intent.extras
        val selectedTeam = intent.getParcelableExtra<Team>("selectedTeam")

        recyclerView = findViewById(R.id.playerListRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        binding.textView.text = selectedTeam?.teamName

        val viewModel : RosterViewModel by viewModels()

        viewModel.getRoster().observe(this) { roster ->
            val adapter = PlayerRecyclerViewAdapter(roster,selectedTeam)
            recyclerView.adapter = adapter
            adapter.setOnItemClickListener(object : PlayerRecyclerViewAdapter.onItemClickListener{
                override fun onItemClick(position: Int) {
                    val myIntent = Intent(this@RosterActivity, PlayerActivity::class.java)
                    myIntent.putExtra("selectedTeam",selectedTeam)
                    myIntent.putExtra("selectedPlayer",roster[position])
                    startActivity(myIntent)
                }
            })
            //Test This Prior To Pushing
            val swipeToDeleteCallback = object : SwipeToDeleteCallback(){
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.bindingAdapterPosition
                    val playerID = roster[position].playerID.toString()
                    if(direction == 8){ //Swipe Right to delete
                        val db = FirebaseFirestore.getInstance().collection("players").document(playerID)
                            .delete()
                            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
                            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
                    }
                    else if (direction == 4){ //Swipe left to edit
                        val myIntent = Intent(this@RosterActivity, EditRosterActivity::class.java)
                        myIntent.putExtra("selectedTeam",selectedTeam)
                        myIntent.putExtra("selectedPlayer",roster[position])
                        startActivity(myIntent)
                    }
                }
            }
            //Test
            val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
            itemTouchHelper.attachToRecyclerView(recyclerView)
        }

        val editRosterButton = binding.editRosterButton

        editRosterButton.setOnClickListener{
            val intent = Intent(this, EditRosterActivity::class.java)
            intent.putExtra("selectedTeam",selectedTeam)
            startActivity(intent)
        }
    }
}