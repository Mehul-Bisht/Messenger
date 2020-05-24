package com.example.messenger.register_login

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.messenger.R
import com.example.messenger.message.LatestMessagesActivity
import com.example.messenger.models.User
import com.example.messenger.models.UserStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {


    val db = Firebase.firestore

    var datetime4: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

         (supportActionBar != null)
            supportActionBar?.hide()

        val animation = constraint_layout2.background as AnimationDrawable

        animation.setEnterFadeDuration(2000)
        animation.setExitFadeDuration(4000)
        animation.start()

        val calendar4: Calendar = Calendar.getInstance()

        val dateformat3 = SimpleDateFormat("dd MMM hh:mm aa")
        datetime4 = dateformat3.format(calendar4.time)
        if (datetime4 != null) {
            Log.d("date and time ", datetime4!!)
        }

        if (password_editext_register.text.toString().isNotEmpty())
            et_ShowHide.visibility = View.VISIBLE

        et_ShowHide.setOnClickListener {


            if (et_ShowHide.text.toString().equals("Show")) {
                password_editext_register.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                et_ShowHide.text = "Hide"
            } else {
                password_editext_register.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                et_ShowHide.text = "Show"

            }

        }

        register_button.setOnClickListener() {

            performregister()
        }

        already_have_account_textview.setOnClickListener() {

            val intent = Intent(this, login::class.java)

            startActivity(intent)
        }

        select_image_button.setOnClickListener() {

            val intent = Intent(Intent.ACTION_PICK)

            intent.type = "image/*"

            startActivityForResult(intent, 0)
        }

        watchtext()


    }


    private fun watchtext(){

        password_editext_register.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val input_password = password_editext_register.text.toString().trim()

                if (input_password.isEmpty()) {
                    et_ShowHide.visibility = View.INVISIBLE
                    et_ShowHide.text = "Show"
                    tv_password_length.visibility = View.INVISIBLE
                    alert_imageview.visibility = View.INVISIBLE

                }


                 else if (input_password.isNotEmpty()) {
                    //et_ShowHide.isEnabled = true
                    et_ShowHide.visibility = View.VISIBLE

                    if (input_password.length < 6) {
                        tv_password_length.visibility = View.VISIBLE
                        //register_button.isEnabled = false
                        alert_imageview.visibility = View.VISIBLE
                    }

                    else {
                        tv_password_length.visibility = View.INVISIBLE
                        //register_button.isEnabled = true
                        alert_imageview.visibility = View.INVISIBLE
                    }

                }
            }
                    override fun afterTextChanged(s: Editable?) {

                    }
        })
    }



    private fun performregister() {
        val username = username_editext_register.text.toString()
        val email = email_editext_register.text.toString()
        val password = password_editext_register.text.toString()

        if (username.isEmpty()) {
            //Toast.makeText(this, "please enter username", Toast.LENGTH_SHORT).show()
         //   username_editext_register.setBackgroundResource(R.drawable.rounded_error_edittext)
            tv_incorrect_details.visibility = View.VISIBLE
        }

        else if (username.isNotEmpty()) {
         //   username_editext_register.setBackgroundResource(R.drawable.rounded_editext)
            tv_incorrect_details.visibility = View.INVISIBLE
        }

        if (email.isEmpty()) {
            //Toast.makeText(this, "please enter email", Toast.LENGTH_SHORT).show()
         //   email_editext_register.setBackgroundResource(R.drawable.rounded_error_edittext)
            tv_incorrect_details.visibility = View.VISIBLE
        }

        else if (email.isNotEmpty()) {
        //    email_editext_register.setBackgroundResource(R.drawable.rounded_editext)
            tv_incorrect_details.visibility = View.INVISIBLE
        }

        if(password.isEmpty()){
            //Toast.makeText(this, "please enter password", Toast.LENGTH_SHORT).show()
           // password_editext_register.setBackgroundResource(R.drawable.rounded_error_edittext)
            tv_incorrect_details.visibility = View.VISIBLE
        }

        else if (password.isNotEmpty()) {
         //   password_editext_register.setBackgroundResource(R.drawable.rounded_editext)
            tv_incorrect_details.visibility = View.INVISIBLE
        }


        if(username.isEmpty() || email.isEmpty() || password.isEmpty())
            return

        if(password.trim().length < 6)
           return

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {

                if (!it.isSuccessful) return@addOnCompleteListener

                 Toast.makeText(this, "registration successful", Toast.LENGTH_SHORT).show()


                val builder = AlertDialog.Builder(this)
                val dialogView = layoutInflater.inflate(R.layout.progressbar, null)
                builder.setView(dialogView)
                builder.setCancelable(false)
                val dialog = builder.create()
                dialog.show()

                    Handler().postDelayed({ dialog.dismiss() }, 3000)

                uploadImagetoFirebaseStorage()
            }
            .addOnFailureListener {

                Toast.makeText(this, "Failed to create user", Toast.LENGTH_SHORT).show()

            }
    }

        var selectedPhotoUri: Uri? = null

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)

            if(resultCode == Activity.RESULT_CANCELED)
                return

            else if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null)

                selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            selectphoto_imageview_circle.setImageBitmap(bitmap)
            select_image_button.alpha = 0f
        }

        private fun uploadImagetoFirebaseStorage() {


            if (selectedPhotoUri == null) {
                //val r = FirebaseStorage.getInstance().getReference("/images/default_user.png")
                val r = FirebaseStorage.getInstance().getReference("/images/default_user.png")
                r.downloadUrl.addOnSuccessListener {
                    saveUsertoFirebaseDatabase(it.toString())
                }
                //return
            }
            else {
            val filename = UUID.randomUUID().toString()
            val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

           ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {

                    ref.downloadUrl.addOnSuccessListener {
                        saveUsertoFirebaseDatabase(it.toString())
                    }
                }
        }
        }

    private fun saveUsertoFirebaseDatabase(profileImageUrl: String){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val statusref = FirebaseDatabase.getInstance().getReference("/user-status/$uid")
        val activestatusref = FirebaseDatabase.getInstance().getReference("/user-active-status/$uid")

        val user = User(
            uid, username_editext_register.text.toString(),
            profileImageUrl, "#FFFFFF"
        )

        val userstatus = UserStatus(
            statusref.key!!,
            "Hey ! I'm new to messenger",
            datetime4!!,
            System.currentTimeMillis() / 1000
        )


        statusref.setValue(userstatus)
        activestatusref.setValue(userstatus)

        ref.setValue(user)
            .addOnSuccessListener {



               Toast.makeText(this, "registration successful", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, LatestMessagesActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }

     /*   val username = username_editext_register.text.toString()

        val userdata = hashMapOf(
            "uid" to uid,
            "username" to username,
            "profileImageUrl" to profileImageUrl
        )



           db.collection("users")
            .add(userdata)
               .addOnSuccessListener{
                Toast.makeText(
                    this, "Successfully Saved your data to Firestore !",
                    Toast.LENGTH_LONG
                ).show()

                val intent = Intent(this, LatestMessagesActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener() {

                Toast.makeText(
                    this,
                    "Sorry! Couldn't save your data to Firestore",
                    Toast.LENGTH_LONG
                ).show()
            }
*/
    }



}



