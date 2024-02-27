package com.example.coaching_app
import android.content.Intent
import android.os.Bundle
import com.example.coaching_app.databinding.PlayerBinding
import com.google.firebase.auth.FirebaseAuth


class PlayerActivity : DrawerBaseActivity()
{
    private lateinit var binding: PlayerBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player)
        binding = PlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.editStatButton.setOnClickListener {

        }

        binding.backButton.setOnClickListener{
            val intent = Intent(this, RosterActivity::class.java)
            startActivity(intent)
        }

    }

}