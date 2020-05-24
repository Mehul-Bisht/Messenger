package com.example.messenger.all_settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import com.example.messenger.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_change_password.*


class ChangePasswordActivity : AppCompatActivity() {


    // val errorshape: Drawable? = getDrawable(R.drawable.rounded_error_edittext)
    // val shape: Drawable? = getDrawable(R.drawable.rounded_editext)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

       supportActionBar!!.title = "Change Password"

        register_button.setOnClickListener {
            changepassword()
        }


        watchtext_for_current_password()

        watchtext_for_new_password()

        watchtext_for_verify_new_password()

        et_ShowHide_old.setOnClickListener {
            if (et_ShowHide_old.text.toString().equals("Show")) {
                old_password.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                et_ShowHide_old.text = "Hide"
            } else {
                old_password.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                et_ShowHide_old.text = "Show"

            }
        }

        et_ShowHide_new.setOnClickListener {
            if (et_ShowHide_new.text.toString().equals("Show")) {
                new_password.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                et_ShowHide_new.text = "Hide"
            } else {
                new_password.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                et_ShowHide_new.text = "Show"

            }
        }

        et_ShowHide_verify_new.setOnClickListener {
            if (et_ShowHide_verify_new.text.toString().equals("Show")) {
                verify_new_password.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                et_ShowHide_verify_new.text = "Hide"
            } else {
                verify_new_password.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                et_ShowHide_verify_new.text = "Show"

            }
        }


    }


    private fun watchtext_for_current_password() {

        old_password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (old_password.text.isEmpty()) {
                    et_ShowHide_old.visibility = View.INVISIBLE
                    et_ShowHide_old.text = "Show"
                } else {
                    et_ShowHide_old.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

    }


    private fun watchtext_for_new_password() {

        new_password.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (new_password.text.isEmpty()) {
                    et_ShowHide_new.visibility = View.INVISIBLE
                    et_ShowHide_new.text = "Show"
                } else {
                    et_ShowHide_new.visibility = View.VISIBLE
                }


                if (new_password.text.isNotEmpty()) {
                    if (new_password.text.length < 6) {
                        error_length.visibility = View.VISIBLE
                        // old_password.background = errorshape
                        // new.setBackgroundResource(R.drawable.rounded_error_edittext)
                    } else
                        error_length.visibility = View.INVISIBLE
                    //old_password.background = shape
                    //   new.setBackgroundResource(R.drawable.rounded_editext)
                } else if (new_password.text.isEmpty()) {
                    error_length.visibility = View.INVISIBLE
                    // new.setBackgroundResource(R.drawable.rounded_editext)
                }

                if (verify_new_password.text.toString() != new_password.text.toString()) {
                    error.visibility = View.VISIBLE

                } else if (verify_new_password.text.toString() == new_password.text.toString()) {
                    error.visibility = View.INVISIBLE
                }


                if (verify_new_password.text.isEmpty() || new_password.text.isEmpty()) {
                    error.visibility = View.INVISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    private fun watchtext_for_verify_new_password() {

        verify_new_password.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (verify_new_password.text.isEmpty()) {
                    et_ShowHide_verify_new.visibility = View.INVISIBLE
                    et_ShowHide_new.text = "Show"
                } else {
                    et_ShowHide_verify_new.visibility = View.VISIBLE
                }


                if (verify_new_password.text.toString() == new_password.text.toString()) {
                    error.visibility = View.INVISIBLE
                } else if (verify_new_password.text.toString() != new_password.text.toString()) {
                    error.visibility = View.VISIBLE
                }


                if (verify_new_password.text.isEmpty() || new_password.text.isEmpty()) {
                    error.visibility = View.INVISIBLE
                }

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }


    private fun changepassword() {
        val email_get = email.text.toString()
        val old_password_get = old_password.text.toString()
        val new_password_get = new_password.text.toString()
        val verify_new_password_get = verify_new_password.text.toString()

        if (email_get.isEmpty()) {
            //Toast.makeText(this, "Enter email to proceed", Toast.LENGTH_SHORT).show()
            alert_email.visibility = View.VISIBLE
            email.setBackgroundResource(R.drawable.rounded_error_edittext)
        }

        if (old_password_get.isEmpty()) {
          //  Toast.makeText(this, "Enter Current Password to proceed", Toast.LENGTH_SHORT).show()
            alert_old.visibility = View.VISIBLE
            old_password.setBackgroundResource(R.drawable.rounded_error_edittext)
        }



        if (new_password_get.isEmpty()) {
           // Toast.makeText(this, "Enter New Password to proceed", Toast.LENGTH_SHORT).show()
            alert_new.visibility = View.VISIBLE
            new_password.setBackgroundResource(R.drawable.rounded_error_edittext)
        } else {
            alert_new.visibility = View.INVISIBLE
            new_password.setBackgroundResource(R.drawable.rounded_editext)
        }

        if (verify_new_password_get.isEmpty()) {
           // Toast.makeText(this, "Re-Enter Password to proceed", Toast.LENGTH_SHORT).show()
            alert_verify_new.visibility = View.VISIBLE
            verify_new_password.setBackgroundResource(R.drawable.rounded_error_edittext)
        } else {
            alert_verify_new.visibility = View.INVISIBLE
            verify_new_password.setBackgroundResource(R.drawable.rounded_editext)
        }

        if (verify_new_password_get != new_password_get) {
            //Toast.makeText(this, "You cannot continue right now !", Toast.LENGTH_SHORT).show()
            return
        }

        if (email_get.isNotEmpty()) {
            alert_email.visibility = View.INVISIBLE
            email.setBackgroundResource(R.drawable.rounded_editext)
        }

        if (old_password_get.isNotEmpty()) {
            alert_old.visibility = View.INVISIBLE
            old_password.setBackgroundResource(R.drawable.rounded_editext)
        }

        if (email_get.isNotEmpty()) {

            if (old_password_get.isNotEmpty()) {

                if (verify_new_password.text.isNotEmpty() || new_password.text.isNotEmpty())
                    if (verify_new_password_get == new_password_get) {
                        //Toast.makeText(this, "You can continue !", Toast.LENGTH_SHORT).show()
                        alert_old.visibility = View.INVISIBLE

                        FirebaseAuth.getInstance().sendPasswordResetEmail(email_get)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    finish()

                                }

                            }
                    }
            }
        } else {
            //Toast.makeText(this, "You cannot continue right now !", Toast.LENGTH_SHORT).show()
            alert_old.visibility = View.VISIBLE
            return
        }

    }

}





