package com.example.coaching_app

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coaching_app.databinding.ActivityPlaybookBinding
import com.google.firebase.firestore.FirebaseFirestore
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

class PlaybookActivity : DrawerBaseActivity() {

    private lateinit var binding: ActivityPlaybookBinding
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playbook)
        binding = ActivityPlaybookBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        allocateActivityTitle("Playbook")

        val bundle: Bundle? = intent.extras
        val selectedTeam = intent.getParcelableExtra<Team>("selectedTeam")

        recyclerView = findViewById(R.id.playRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        val viewModel : PlaybookViewModel by viewModels()
        viewModel.getPlayBook().observe(this) { playbook ->
            val adapter = PlaybookRecyclerViewAdapter(playbook,selectedTeam)
            recyclerView.adapter = adapter

            val swipeToDeleteCallback = object : SwipeToDeleteCallback(){
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                    // delete items
                    if(direction == 8){
                        // create a yes/no confirmation to delete the game hist
                        val builder = AlertDialog.Builder(this@PlaybookActivity)
                        builder.setTitle("Confirm Delete")
                        builder.setMessage("Are you sure you want to delete this play? It will not come back!!!")

                        builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, i ->

                            val position = viewHolder.bindingAdapterPosition
                            val playID = playbook[position].playID.toString()
                            val db = FirebaseFirestore.getInstance().collection("play_book").document(playID)
                                .delete()

                        })

                        builder.setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, i ->
                            dialogInterface.cancel()
                            adapter.notifyItemChanged(viewHolder.bindingAdapterPosition)
                        })

                        val alert = builder.create()
                        alert.show()
                    }

                    else{
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
                                this@PlaybookActivity,
                                R.color.background_del
                            )

                        )
                        .addSwipeLeftBackgroundColor(
                            ContextCompat.getColor(
                                this@PlaybookActivity,
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

            val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
            itemTouchHelper.attachToRecyclerView(recyclerView)
        }

        val createPlayButton = binding.createPlayButton

        createPlayButton.setOnClickListener{
            val intent = Intent(this, CreatePlaybookActivity::class.java)
            intent.putExtra("selectedTeam",selectedTeam)
            startActivity(intent)
        }
    }
}