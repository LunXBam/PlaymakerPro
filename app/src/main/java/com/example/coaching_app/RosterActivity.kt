package com.example.coaching_app

import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coaching_app.databinding.ActivityRosterBinding
import com.google.firebase.firestore.FirebaseFirestore
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

class RosterActivity : DrawerBaseActivity() {
    private lateinit var binding: ActivityRosterBinding
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_roster)
        binding = ActivityRosterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        allocateActivityTitle("Team Roster")

        //supportActionBar?.title = "Team Roster"
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val bundle: Bundle? = intent.extras
        val selectedTeam = intent.getParcelableExtra<Team>("selectedTeam")

        recyclerView = findViewById(R.id.playerListRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        binding.textView.text = selectedTeam?.teamName
        val city = binding.cityNameTextView
        city.text = selectedTeam?.city
        val sport = binding.sportNameTextView
        sport.text = selectedTeam?.sport

        val teamLogoString = selectedTeam?.logo
        val imageView = findViewById<ImageView>(R.id.imageView)
        val bitmap = base64ToBitmap(teamLogoString)
        if (bitmap != null)
        {
            imageView.setImageBitmap(bitmap)
        } else
        {
            imageView.setImageResource(R.drawable.default_team_logo)
        }


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

                        val builder = AlertDialog.Builder(this@RosterActivity)
                        builder.setTitle("Confirm Delete")
                        builder.setMessage("Are you sure you want to delete this game history? It will not come back!!!")

                        // If they confirm they want to delete, delete the game history
                        builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, i ->
                            val db = FirebaseFirestore.getInstance().collection("players").document(playerID)
                                .delete()
                                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
                                .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }

                            val query = FirebaseFirestore.getInstance().collection("soccer_stats")
                                .whereEqualTo(playerID,playerID)
                                .get()
                                .addOnSuccessListener { documents ->
                                    for (document in documents) {
                                        document.reference.delete()
                                    }
                                }
                                .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
                        })

                        // if they confirm they don't want to delete
                        builder.setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, i ->
                            dialogInterface.cancel()
                            adapter.notifyItemRemoved(viewHolder.bindingAdapterPosition)
                        })

                        val alert = builder.create()
                        alert.show()


                    }
                    else if (direction == 4){ //Swipe left to edit
                        val myIntent = Intent(this@RosterActivity, EditRosterActivity::class.java)
                        myIntent.putExtra("selectedTeam",selectedTeam)
                        myIntent.putExtra("selectedPlayer",roster[position])
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
                                this@RosterActivity,
                                R.color.background_del
                            )

                        )
                        .addSwipeLeftBackgroundColor(
                            ContextCompat.getColor(
                                this@RosterActivity,
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

        val editRosterButton = binding.editRosterButton

        editRosterButton.setOnClickListener{
            val intent = Intent(this, EditRosterActivity::class.java)
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