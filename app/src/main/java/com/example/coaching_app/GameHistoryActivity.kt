package com.example.coaching_app

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coaching_app.databinding.GameHistoryBinding
import com.google.firebase.firestore.FirebaseFirestore

class GameHistoryActivity : DrawerBaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: GameHistoryBinding


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_history)
        binding = GameHistoryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        recyclerView = findViewById(R.id.gameHistRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        val bundle: Bundle? = intent.extras
        val selectedTeam = intent.getParcelableExtra<Team>("selectedTeam")

        binding.TeamName.text = selectedTeam?.teamName

       val viewModel : GameHistViewModel by viewModels()

        viewModel.getGameHistory().observe(this) { gameHist ->
            val adapter = GameHistoryRecyclerViewAdapter(gameHist,selectedTeam)
            recyclerView.adapter = adapter

//            adapter.setOnItemClickListener(object : GameHistoryRecyclerViewAdapter.onItemClickListener{
//                override fun onItemClick(position: Int) {
//                    val myIntent = Intent(this@GameHistoryActivity, EditDeleteGameHistActivity::class.java)
//                    myIntent.putExtra("selectedTeam", selectedTeam)
//                    myIntent.putExtra("selectedGame",gameHist[position])
//                    startActivity(myIntent)
//                }
//            })


            val swipeToDeleteCallback = object : SwipeToDeleteCallback(){
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                    if(direction == 8){

                        // create a yes/no confirmation to delete the game hist
                        val builder = AlertDialog.Builder(this@GameHistoryActivity)
                        builder.setTitle("Confirm Delete")
                        builder.setMessage("Are you sure you want to delete this game history? It will not come back!!!")

                        // If they confirm they want to delete, delete the game history
                        builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, i ->

                            val position = viewHolder.bindingAdapterPosition
                            val gameID = gameHist[position].gameID.toString()
                            val db = FirebaseFirestore.getInstance().collection("game_history").document(gameID)
                                .delete()
                                .addOnSuccessListener { Log.d(ContentValues.TAG, "DocumentSnapshot successfully deleted!") }
                                .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error deleting document", e) }

                        })

                        // if they confirm they don't want to delete
                        builder.setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, i ->
                            dialogInterface.cancel()
                        })

                        val alert = builder.create()
                        alert.show()

                    }

                    else {
                        val position = viewHolder.bindingAdapterPosition
                        val myIntent = Intent(this@GameHistoryActivity, EditDeleteGameHistActivity::class.java)
                        myIntent.putExtra("selectedTeam", selectedTeam)
                        myIntent.putExtra("selectedGame",gameHist[position])
                        startActivity(myIntent)
                    }
                }
            }

            val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
            itemTouchHelper.attachToRecyclerView(recyclerView)
        }

        binding.addGame.setOnClickListener{
            val intent = Intent(this, CreateGameHistoryActivity::class.java)
            intent.putExtra("selectedTeam",selectedTeam)
            startActivity(intent)
        }

        binding.goBack.setOnClickListener{
            val intent = Intent(this,LandingPageActivity::class.java)
            intent.putExtra("selectedTeam",selectedTeam)
            startActivity(intent)
        }



    }
}