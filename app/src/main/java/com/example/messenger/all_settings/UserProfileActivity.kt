package com.example.messenger.all_settings

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.messenger.R
import com.example.messenger.message.LatestMessagesActivity
import com.example.messenger.models.User
import com.example.messenger.models.UserStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.my_account_row.view.*
import java.io.ByteArrayOutputStream
import java.util.*


class UserProfileActivity : AppCompatActivity() {


    var emailId : String? = null

    var presentuser: User? = null

    var changeref : DatabaseReference? = null

    var requiredUid : String? = null

    var requiredUri : String? = null

    var statusvariable : UserStatus? = null

    lateinit var imgdecoder : ImageDecoder.Source

    lateinit var bmp : Bitmap

    lateinit var boas : ByteArrayOutputStream

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        //getcurrentuser()

        save.visibility = View.INVISIBLE


        supportActionBar?.title = "My account"
        account_recyclerview.adapter = myAccountAdapter

         //presentuser = intent.getParcelableExtra<User>("KEY")

        //changeref = FirebaseDatabase.getInstance().getReference("/users/$requiredUid")
        //changeref!!.keepSynced(true)

        /*
          val uid = FirebaseAuth.getInstance().uid

          val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

          ref.addListenerForSingleValueEvent(object : ValueEventListener {

              override fun onDataChange(p0: DataSnapshot) {
                  presentuser = p0.getValue(User::class.java)

              }

              override fun onCancelled(p0: DatabaseError) {

              }
          })  */


        presentuser = LatestMessagesActivity.currentUser

        if (presentuser != null){
            requiredUid = presentuser!!.uid

        requiredUri = presentuser!!.profileImageUrl

        Picasso.get().load(presentuser!!.profileImageUrl).into(imageView3)

        //status.text = presentuser!!.status

           FirebaseDatabase.getInstance().getReference("/user-active-status/${presentuser!!.uid}").addValueEventListener(object : ValueEventListener{
               override fun onCancelled(p0: DatabaseError) {

               }

               override fun onDataChange(p0: DataSnapshot) {
                   statusvariable = p0.getValue(UserStatus::class.java)
                   status.text = statusvariable?.statusText
                   savedat.text = statusvariable?.statusDateAndTime
               }

           })

            Log.d("presentuser","is not null")
    }

        pickimage.setOnClickListener(){
            Toast.makeText(this,"select image",Toast.LENGTH_SHORT).show()
            val intent = Intent(Intent.ACTION_PICK)

            intent.type = "image/*"

            startActivityForResult(intent, 1)

        }

        save.setOnClickListener(){
            uploadimage()
        }

        edit_status_icon.setOnClickListener(){
            val intent = Intent(this,
                StatusEditActivity::class.java)
         //   intent.putExtra("requiredUID",requiredUid)
            startActivity(intent)
          //  finish()
        }

        emailId = FirebaseAuth.getInstance().currentUser?.email

        //account_recyclerview.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
        createDummydata()
        //imageView4.alpha

 /*       status_save.setOnClickListener(){
            if(status.text.toString().isEmpty()){
               return@setOnClickListener
            }

            FirebaseDatabase.getInstance().getReference("/users").child("/$requiredUid")
                .child("/status").setValue(status.text.toString())

        }
*/


    }

    val myAccountAdapter = GroupAdapter<GroupieViewHolder>()

    private fun getcurrentuser() {
        val uid = FirebaseAuth.getInstance().uid

        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                presentuser = p0.getValue(User::class.java)
                Log.d("current uid =", presentuser?.uid)
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })


        initialiseUser()
      /*  requiredUid = presentuser!!.uid

        requiredUri = presentuser!!.profileImageUrl

        Picasso.get().load(presentuser!!.profileImageUrl).into(imageView3)

        status.text = presentuser!!.status
*/
    }

    private fun initialiseUser(){
         requiredUid = presentuser!!.uid

       requiredUri = presentuser!!.profileImageUrl

       Picasso.get().load(presentuser!!.profileImageUrl).into(imageView3)

      // status.text = presentuser!!.status

        Log.d("presentuser", "is not null")
    }

    private fun createDummydata() {

      //  myAccountAdapter.add(MyAccount("", ""))
       // myAccountAdapter.add(MyAccount("", ""))
        if(presentuser!=null)
        myAccountAdapter.add(
            MyAccount(
                "Username : ",
                presentuser!!.username
            )
        )
        myAccountAdapter.add(
            MyAccount(
                "Email : ",
                emailId!!
            )
        )

    }


    var changePhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_CANCELED)
            return
        else if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null)

            changePhotoUri = data.data

        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, changePhotoUri)


   /*     if(changePhotoUri!=null)
        {  imgdecoder = ImageDecoder.createSource(contentResolver, changePhotoUri!!)
           bmp = ImageDecoder.decodeBitmap(imgdecoder)
            bmp.compress(Bitmap.CompressFormat.JPEG,30,boas)
        }
*/
        //val compressedImgBitmap: Bitmap = Compressor(this).compressToBitmap(actualImageFile)
       // val out = ByteArrayOutputStream()
       // bitmap.compress(Bitmap.CompressFormat.PNG, 50, out)

        imageView3.setImageBitmap(bitmap)
        save.visibility = View.VISIBLE

    }

    private fun uploadimage() {
        val filename = UUID.randomUUID().toString()
       val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

   // val URI: String? = null

    //changeref?.setValue(changePhotoUri)
        //ref.putBytes(boas.toByteArray())

    ref.putFile(changePhotoUri!!)
        .addOnSuccessListener{

        ref.downloadUrl.addOnSuccessListener {
            saveimage(it.toString())
        }

    }

    }

    private fun saveimage( string : String ){

      //  val user = User(presentuser!!.uid, presentuser!!.username, string,"Hey ! I'm new to messenger !")
      //  changeref?.setValue(user)

        FirebaseDatabase.getInstance().getReference("/users").child("/$requiredUid")
            .child("/profileImageUrl").setValue(string)


        save.visibility = View.INVISIBLE

    }



    class MyAccount(val Fname: String, val Fvalue: String) : Item<GroupieViewHolder>() {

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.fieldname.text = Fname
            viewHolder.itemView.fieldvalue.text = Fvalue
        }

        override fun getLayout(): Int {
            return R.layout.my_account_row
        }
    }

 /*   class StatusShower(val userpassed : User) : Item<GroupieViewHolder>(){
        override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        }

        override fun getLayout(): Int {
            return R.layout
        }
    }
*/


}


