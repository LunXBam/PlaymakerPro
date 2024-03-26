package com.example.coaching_app

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.firebase.ui.auth.AuthUI
import com.google.android.material.navigation.NavigationView

open class DrawerBaseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer_base)
    }

    override fun setContentView(view: View?) {
        drawerLayout = layoutInflater.inflate(R.layout.activity_drawer_base, null) as DrawerLayout
        val container: FrameLayout = drawerLayout.findViewById(R.id.activityContainer)
        container.addView(view)
        super.setContentView(drawerLayout)

        val toolbar: Toolbar = drawerLayout.findViewById(R.id.toolBar)
        setSupportActionBar(toolbar)

        val navigationView: NavigationView = drawerLayout.findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.menu_drawer_open,
            R.string.menu_drawer_close
        )
        toggle.syncState()
    }

//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        return false
//    }



    val recievedData = DataRepo.sharedData
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawer(GravityCompat.START)
        when (item.itemId) {
            R.id.nav_home -> {

                val intent = Intent(this, LandingPageActivity::class.java)
                intent.putExtra("selectedTeam", recievedData)
                startActivity(intent)
//                startActivity(Intent(this, LandingPageActivity::class.java))
            }
            R.id.nav_team -> {
                val intent = Intent(this, RosterActivity::class.java)
                intent.putExtra("selectedTeam", recievedData)
                startActivity(intent)
//                startActivity(Intent(this, RosterActivity::class.java))
            }
            R.id.nav_history -> {
                val intent = Intent(this, GameHistoryActivity::class.java)
                intent.putExtra("selectedTeam", recievedData)
                startActivity(intent)
//                startActivity(Intent(this, GameHistoryActivity::class.java))
            }
            R.id.nav_playbook -> {
                val intent = Intent(this, PlaybookActivity::class.java)
                intent.putExtra("selectedTeam", recievedData)
                startActivity(intent)
//                startActivity(Intent(this, PlaybookActivity::class.java))
            }
            R.id.nav_logout ->{
                AuthUI.getInstance()
                    .signOut(this)
                    .addOnSuccessListener{
                        startActivity(Intent(this,Login::class.java))

                    }.addOnFailureListener {
                        Toast.makeText(this,"Didn't work", Toast.LENGTH_LONG).show()
                    }

            }
            R.id.nav_team_select ->{
                startActivity(Intent(this,TeamSelectActivity::class.java))
            }
            R.id.nav_scheduler ->{
                startActivity(Intent(this,SchedulerActivity::class.java))
            }
        }
        return true
    }


    protected fun allocateActivityTitle(titleString: String) {
        supportActionBar?.title = titleString
    }
}


