package com.example.messenger.all_settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.messenger.R
import com.example.messenger.message.LatestMessagesActivity
import com.example.messenger.models.UserStatus
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_status_edit.*
import kotlinx.android.synthetic.main.previous_status_row.view.*
import java.text.SimpleDateFormat
import java.util.*

class StatusEditActivity : AppCompatActivity() {

    val statusadapter = GroupAdapter<GroupieViewHolder>()

    var datetime3: String? = null

    var requiredstatusref : DatabaseReference? = null

    var requiredstatus : UserStatus? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status_edit)

        supportActionBar?.title = "Update Status"

        statusrecyclerview.adapter = statusadapter

        //val requiredUID = intent.getStringExtra("requiredUID")
        val requiredUID = LatestMessagesActivity.currentUser?.uid
        requiredstatusref = FirebaseDatabase.getInstance().getReference("/user-active-status/$requiredUID")



        requiredstatusref?.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                requiredstatus = p0.getValue(UserStatus::class.java)
                if(requiredstatus!=null) {
                    statusadapter.add(
                        previousStatus(
                            requiredstatus!!
                        )
                    )
                    type_your_status.hint = requiredstatus!!.statusText
                }
            }

        })

        update_status.visibility = View.INVISIBLE

        watchstatus()

        val calendar3: Calendar = Calendar.getInstance()

        val dateformat3 = SimpleDateFormat("dd MMM hh:mm aa")
        datetime3 = dateformat3.format(calendar3.time)
        if(datetime3 != null)
        {  Log.d("date and time ",datetime3!!) }

        update_status.setOnClickListener(){
            if (type_your_status.text.toString().isEmpty()) {
                return@setOnClickListener
            }


           // FirebaseDatabase.getInstance().getReference("/users").child("/$requiredUID")
           //     .child("/status").setValue(type_your_status.text.toString())

         //   FirebaseDatabase.getInstance().getReference("/users").child("/$requiredUID")
          //      .child("/status").setValue(type_your_status.text.toString())


            val statustobesavedref =
                FirebaseDatabase.getInstance().getReference("/user-active-status/$requiredUID")

            val statustobesaved = UserStatus(
                statustobesavedref.key!!,
                type_your_status.text.toString(),
                datetime3!!,
                System.currentTimeMillis() / 1000
            )

                statustobesavedref.setValue(statustobesaved)

            FirebaseDatabase.getInstance().getReference("/user-status/$requiredUID").push()
                .setValue(statustobesaved)

            Toast.makeText(this,"status has been saved !",Toast.LENGTH_SHORT).show()

            val intent = Intent(this,
                LatestMessagesActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            //finish()


       /*     val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.progressbar, null)
            builder.setView(dialogView)
            builder.setCancelable(false)
            val dialog = builder.create()
            dialog.show()

            Handler().postDelayed({ dialog.dismiss() }, 5000)
*/
        }

    }
    private fun watchstatus(){
        type_your_status.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (type_your_status.text.toString().isEmpty()){
                    update_status.visibility = View.INVISIBLE
                    return
            }
                if (type_your_status.text.toString().isNotEmpty()) {
                      update_status.visibility = View.VISIBLE
                }
            }
        })
    }

    class previousStatus( val statuswalauser : UserStatus) : Item<GroupieViewHolder>() {

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.Status_Date.text = statuswalauser.statusDateAndTime
            viewHolder.itemView.Status_text.text = statuswalauser.statusText
        }

        override fun getLayout(): Int {
         return R.layout.previous_status_row
        }
    }
}
