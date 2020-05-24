package com.example.messenger.all_settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import com.example.messenger.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    var color_to_be_saved : String = "#FFFFFF"

    lateinit var animation : Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar?.title = "Settings"

        val Uid = FirebaseAuth.getInstance().uid

        first.setOnClickListener(){
          //  FirebaseDatabase.getInstance().getReference("/users").child("/$Uid")
           //     .child("/colorString").setValue("#008577")
            theme_save.visibility = View.VISIBLE
            theme_cancel.visibility = View.VISIBLE
            color_to_be_saved = "#008577"
            first.animate().scaleX(1.1f).scaleY(1.1f).setDuration(300).start()
            second.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
            third.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
            fourth.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
            fifth.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
            sixth.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
        }

        second.setOnClickListener() {
            //FirebaseDatabase.getInstance().getReference("/users").child("/$Uid")
            //    .child("/colorString").setValue("#19D80C")
            theme_save.visibility = View.VISIBLE
            theme_cancel.visibility = View.VISIBLE
            color_to_be_saved = "#BD590B"
            first.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
            second.animate().scaleX(1.1f).scaleY(1.1f).setDuration(300).start()
            third.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
            fourth.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
            fifth.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
            sixth.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
        }

        third.setOnClickListener() {
          //  FirebaseDatabase.getInstance().getReference("/users").child("/$Uid")
            //    .child("/colorString").setValue("#0881EA")
            theme_save.visibility = View.VISIBLE
            theme_cancel.visibility = View.VISIBLE
            color_to_be_saved = "#0881EA"
            first.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
            second.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
            third.animate().scaleX(1.1f).scaleY(1.1f).setDuration(300).start()
            fourth.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
            fifth.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
            sixth.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
        }

        fourth.setOnClickListener() {
          //  FirebaseDatabase.getInstance().getReference("/users").child("/$Uid")
           //     .child("/colorString").setValue("#DD1212")
            theme_save.visibility = View.VISIBLE
            theme_cancel.visibility = View.VISIBLE
            color_to_be_saved = "#DD1212"
            first.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
            second.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
            third.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
            fourth.animate().scaleX(1.1f).scaleY(1.1f).setDuration(300).start()
            fifth.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
            sixth.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
        }

        fifth.setOnClickListener() {
        //    FirebaseDatabase.getInstance().getReference("/users").child("/$Uid")
           //     .child("/colorString").setValue("#000000")
            theme_save.visibility = View.VISIBLE
            theme_cancel.visibility = View.VISIBLE
            color_to_be_saved = "#000000"
            first.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
            second.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
            third.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
            fourth.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
            fifth.animate().scaleX(1.1f).scaleY(1.1f).setDuration(300).start()
            sixth.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
        }

        sixth.setOnClickListener() {
           // FirebaseDatabase.getInstance().getReference("/users").child("/$Uid")
           //     .child("/colorString").setValue("#FFFFFF")
            theme_save.visibility = View.VISIBLE
            theme_cancel.visibility = View.VISIBLE
            color_to_be_saved = "#FFFFFF"
            first.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
            second.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
            third.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
            fourth.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
            fifth.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
            sixth.animate().scaleX(1.1f).scaleY(1.1f).setDuration(300).start()
        }

        theme_save.setOnClickListener(){
            FirebaseDatabase.getInstance().getReference("/users").child("/$Uid")
                .child("/colorString").setValue(color_to_be_saved)
            theme_save.visibility = View.INVISIBLE
            theme_cancel.visibility = View.INVISIBLE
           // val animation2 = AnimationUtils.loadAnimation(this, R.anim.zoom_out)
           // first.startAnimation(animation2)
        }

        theme_cancel.setOnClickListener(){
            theme_save.visibility = View.INVISIBLE
            theme_cancel.visibility = View.INVISIBLE
            first.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
            second.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
            third.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
            fourth.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
            fifth.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
            sixth.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
        }
    }
}
