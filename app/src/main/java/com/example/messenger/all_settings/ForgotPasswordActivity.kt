package com.example.messenger.all_settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import com.example.messenger.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)


        supportActionBar!!.title = "Forgot Password"

        forgot_email_button.setOnClickListener {

            if (email_forgot.text.toString().isEmpty())
                email_forgot.setBackgroundResource(R.drawable.rounded_error_edittext)

        if (email_forgot.text.toString().isNotEmpty()) {
            email_forgot.setBackgroundResource(R.drawable.rounded_editext)
            FirebaseAuth.getInstance().sendPasswordResetEmail(email_forgot.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        val toast : Toast? = null

                        toast!!.setGravity(Gravity.CENTER_VERTICAL, 0, 0)

                        Toast.makeText(this,"Password Reset email has been sent",Toast.LENGTH_LONG).show()



                        //toast.show()
                    }
                }
        }
    }

    }
}
