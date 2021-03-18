package com.britishbroadcast.frindershare.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.britishbroadcast.frindershare.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        Log.d("TAG_X", "Logged...in.....")

        logout_button.setOnClickListener {

            Log.d("TAG_X", "Logging out...")
            FirebaseAuth.getInstance().signOut()

            val lIntent = Intent(this, LoginActivity::class.java)
            lIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(lIntent)

        }
    }
}