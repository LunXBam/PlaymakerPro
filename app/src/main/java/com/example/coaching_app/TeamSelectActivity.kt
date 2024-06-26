package com.example.coaching_app

import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coaching_app.databinding.ActivityTeamSelectBinding
import com.google.firebase.firestore.FirebaseFirestore
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

class TeamSelectActivity : DrawerBaseActivity() {
    private lateinit var binding: ActivityTeamSelectBinding
    private lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_select)
        binding = ActivityTeamSelectBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        allocateActivityTitle("Select Team")
        isNavDrawerEnabled = false

        //supportActionBar?.title = "Team Roster"
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val bundle: Bundle? = intent.extras
        val userID: String? = intent.getStringExtra("userID")

        recyclerView = findViewById(R.id.teamListRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)



        val viewModel : TeamSelectViewModel by viewModels()
        viewModel.getTeams().observe(this) { teams ->
            val adapter = TeamSelectRecyclerViewAdapter(teams)
            recyclerView.adapter = adapter
            adapter.setOnItemClickListener(object : TeamSelectRecyclerViewAdapter.onItemClickListener{
                override fun onItemClick(position: Int) {
                    //Toast.makeText(this@TeamSelectActivity,teams[position].teamName,Toast.LENGTH_LONG).show()
                    val myIntent = Intent(this@TeamSelectActivity, LandingPageActivity::class.java)

                    myIntent.putExtra("selectedTeam",teams[position])
                    val datashare = teams[position]
                    DataRepo.sharedData = datashare
                    startActivity(myIntent)
                }

            })

            val swipeToDeleteCallback = object : SwipeToDeleteCallback(){
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.bindingAdapterPosition
                    val teamID = teams[position].teamID.toString()
                    if(direction == 8){ //Swipe Right to delete

                        val builder = AlertDialog.Builder(this@TeamSelectActivity)
                        builder.setTitle("Confirm Delete")
                        builder.setMessage("Are you sure you want to delete this team? It will not come back!!!")

                        // If they confirm they want to delete, delete the game history
                        builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, i ->
                            val db = FirebaseFirestore.getInstance().collection("teams").document(teamID)
                                .delete()
                                .addOnSuccessListener { Log.d(ContentValues.TAG, "DocumentSnapshot successfully deleted!") }
                                .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error deleting document", e) }

                        })

                        // if they confirm they don't want to delete
                        builder.setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, i ->
                            dialogInterface.cancel()
                            adapter.notifyItemChanged(viewHolder.bindingAdapterPosition)
                        })

                        val alert = builder.create()
                        alert.show()


                    }
                    else if (direction == 4){ //Swipe left to edit
                        val myIntent = Intent(this@TeamSelectActivity, CreateTeamActivity::class.java)
                        myIntent.putExtra("selectedTeam",teams[position])
                        startActivity(myIntent)
                        adapter.notifyItemRemoved(viewHolder.bindingAdapterPosition)
                    }
                }
                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {
                    RecyclerViewSwipeDecorator.Builder(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                        .addSwipeRightBackgroundColor(
                            ContextCompat.getColor(
                                this@TeamSelectActivity,
                                R.color.background_del
                            )

                        )
                        .addSwipeLeftBackgroundColor(
                            ContextCompat.getColor(
                                this@TeamSelectActivity,
                                R.color.background_edit
                            )
                        )

                        .addSwipeRightActionIcon(R.drawable.baseline_arrow_back_24)
                        .addSwipeLeftActionIcon(R.drawable.baseline_add_box_24)
                        .create()
                        .decorate()

                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }
            }
            //Test
            val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
            itemTouchHelper.attachToRecyclerView(recyclerView)
        }


        val newTeamButton = binding.newTeamButton

        newTeamButton.setOnClickListener{
            val intent = Intent(this, CreateTeamActivity::class.java)
            startActivity(intent)
        }
    }
}