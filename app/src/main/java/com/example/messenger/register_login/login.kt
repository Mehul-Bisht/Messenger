package com.example.messenger.register_login

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import com.example.messenger.all_settings.ForgotPasswordActivity
import com.example.messenger.R
import com.example.messenger.message.LatestMessagesActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class login : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val animation = constraint_layout.background as AnimationDrawable

        animation.setEnterFadeDuration(2000)
        animation.setExitFadeDuration(4000)
        animation.start()

        if(supportActionBar != null)
        supportActionBar?.hide()

        et_Showhide.setOnClickListener {
            if (et_Showhide.text.toString().equals("Show")) {
                password_editext_login.transformationMethod = HideReturnsTransformationMethod.getInstance()
                et_Showhide.text = "Hide"
            } else {
                password_editext_login.transformationMethod = PasswordTransformationMethod.getInstance()
                et_Showhide.text = "Show"

            }
        }


        login_button.setOnClickListener(){


            val email = email_editext_login.text.toString()
            val password = password_editext_login.text.toString()

          /*  if (email.isNotEmpty()) {
                email_editext_login.setBackgroundResource(R.drawable.rounded_editext)
                error_login.visibility = View.INVISIBLE
            }

            if (password.isNotEmpty()) {
                password_editext_login.setBackgroundResource(R.drawable.rounded_editext)
                error_login.visibility = View.INVISIBLE
            }

            if (email.isEmpty()) {
                email_editext_login.setBackgroundResource(R.drawable.rounded_error_edittext)
                error_login.visibility = View.VISIBLE
            }

            if (password.isEmpty()) {
                password_editext_login.setBackgroundResource(R.drawable.rounded_error_edittext)
                error_login.visibility = View.VISIBLE
            }
            */

            if(email.isEmpty() || password.isEmpty())
            {   Toast.makeText(this,"Please check the entered Email and password !",Toast.LENGTH_SHORT).show()
                return@setOnClickListener   }




            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener {
                    if (!it.isSuccessful)
                        return@addOnCompleteListener


                        else{
                        error_login.visibility = View.INVISIBLE
                       Toast.makeText(this, "login successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LatestMessagesActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                }
                .addOnFailureListener{
                    //Toast.makeText(this,"Failed to login User", Toast.LENGTH_SHORT).show()
                    error_login.visibility = View.VISIBLE
                }
        }


        back_to_registration_textview.setOnClickListener(){
          finish()
      }

        forgot_password_textview.setOnClickListener{

         val intent = Intent(this, ForgotPasswordActivity::class.java)
         startActivity(intent)

        }

        watchtext()
    }


    private fun watchtext() {

        password_editext_login.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val input_password = password_editext_login.text.toString().trim()

                if (input_password.isEmpty()) {//et_ShowHide.isEnabled = false
                    et_Showhide.visibility = View.INVISIBLE
                    et_Showhide.text = "Show"

                } else if (input_password.isNotEmpty())
                //et_ShowHide.isEnabled = true
                    et_Showhide.visibility = View.VISIBLE

            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }
}
