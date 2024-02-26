package com.example.coaching_app

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coaching_app.databinding.ActivityPlaybookBinding

class PlaybookActivity : DrawerBaseActivity() {

    private lateinit var binding: ActivityPlaybookBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var playsList : ArrayList<PlaybookModel>
    private lateinit var imageList:Array<Int>
    private lateinit var titleList:Array<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playbook)
        binding = ActivityPlaybookBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        recyclerView = findViewById(R.id.playRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        playsList = arrayListOf<PlaybookModel>()

        loadData()
    }

    private fun loadData(){

        for(i in titleList.indices){
            playsList.add(PlaybookModel(imageList[i],titleList[i].toString()))
        }
        recyclerView.adapter = PlaybookRecyclerViewAdapter(playsList)
    }
}