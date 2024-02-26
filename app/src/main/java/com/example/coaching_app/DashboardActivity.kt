package com.example.coaching_app

import android.os.Bundle
import com.example.coaching_app.databinding.ActivityDashboardBinding

class DashboardActivity : DrawerBaseActivity() {

    private lateinit var activityDashboardBinding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityDashboardBinding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(activityDashboardBinding.root)
        allocateActivityTitle("Dashboard")
    }
}