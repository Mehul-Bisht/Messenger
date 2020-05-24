package com.example.messenger.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.messenger.R
import com.example.messenger.message.LatestMessagesActivity

class SplashScreen_Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen_)


        if(supportActionBar != null)
            supportActionBar?.hide()

        Handler().postDelayed({
            val intent = Intent(this,
                LatestMessagesActivity::class.java)
            startActivity(intent)
            overridePendingTransition(
                R.anim.fade_in,
                R.anim.fade_out
            )
            finish()
        },500)

       // val intent = Intent(this, LatestMessagesActivity::class.java)
       // startActivity(intent)

    }
}
