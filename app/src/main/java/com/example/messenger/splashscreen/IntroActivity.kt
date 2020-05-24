package com.example.messenger.splashscreen

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.messenger.R
import com.example.messenger.register_login.MainActivity
import com.example.messenger.register_login.login
import kotlinx.android.synthetic.main.activity_intro.*
import kotlinx.android.synthetic.main.activity_intro.register_button

class IntroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        if(supportActionBar != null)
        supportActionBar?.hide()


        val animation = constraint_layout3.background as AnimationDrawable

        animation.setEnterFadeDuration(2000)
        animation.setExitFadeDuration(4000)
        animation.start()

        login_button.setOnClickListener{
            val intent = Intent(this,
                login::class.java)
            startActivity(intent)
        }

        register_button.setOnClickListener{
            val intent = Intent(this,
                MainActivity::class.java)
            startActivity(intent)
        }
    }
}
