package com.example.messenger.message

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.messenger.R
import com.example.messenger.models.ChatMessage
import com.example.messenger.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class ChatLogActivity : AppCompatActivity() {

    val adapter = GroupAdapter<GroupieViewHolder>()

    var toUser: User? = null

    var userID : String? = null
    var userfrom : User? = null

    var senderDocId : String? = null
    var recieverDocId: String? = null
    var senderDId : String? = null
    var receiverDId : String? = null
    var TextGetFromDoc: String? = null

    var getcolorString : String? = null

    var NameOfDay : String? = null

    var datetime : String? = null

    var datetime2 : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        recyclerView_chat_log.adapter = adapter

        toUser = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)

        supportActionBar?.title = toUser?.username


        if(LatestMessagesActivity.currentUser!=null)
        {
            getcolorString = LatestMessagesActivity.currentUser!!.colorString
            Log.d("colorstring is",getcolorString)
        recyclerView_chat_log.setBackgroundColor(Color.parseColor(getcolorString))
        bottom.setBackgroundColor(Color.parseColor(getcolorString))
        }

        val calendar1: Calendar = Calendar.getInstance()

        when (calendar1.get(Calendar.DAY_OF_WEEK)) {
            1 -> NameOfDay = "Sunday"
            2 -> NameOfDay = "Monday"
            3 -> NameOfDay = "Tuesday"
            4 -> NameOfDay = "Wednesday"
            5 -> NameOfDay = "Thursday"
            6 -> NameOfDay = "Friday"
            7 -> NameOfDay = "Saturday"
        }

        val dateformat = SimpleDateFormat("hh:mm aa")
        datetime = dateformat.format(calendar1.time)

        val dateformat2 = SimpleDateFormat("dd MMM")
        datetime2 = dateformat2.format(calendar1.time)
       // Log.d("DATE is : ",datetime2!!)

        listenformessages()

        send_button_chat_log.setOnClickListener() {
            performsendmessage()
        }

        image_send_button_chat_log.setOnClickListener(){
            Toast.makeText(this,"Image messages will be supported soon",Toast.LENGTH_SHORT).show()
        }
    }


    private fun listenformessages() {

        val fromId = FirebaseAuth.getInstance().uid

        val toId = toUser?.uid

    /*    Firebase.firestore.collection("users")
            .whereEqualTo("uid", fromId)
            .get()
            .addOnSuccessListener { result->
                for (document in result){
                    if (document.exists())
                        senderDId = document.id

                }
            }

        Firebase.firestore.collection("users")
            .whereEqualTo("uid", toId)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.exists())
                        receiverDId = document.id

                }
            }
        */
/*
        Firebase.firestore.collection("user-messages").document(fromId!!).collection(toId!!)
           // .whereEqualTo("senderDocId", senderDId)
          //  .whereEqualTo("recieverDocId", receiverDId)
           // .orderBy("timestamp")
            .get()
            .addOnSuccessListener {result->
                for(document in result)
                if(document.exists()){
                   TextGetFromDoc = document.getString("text")

                    val UserSend = User(
                        document.getString("uid")!!, document.getString("username")!!,
                        document.getString("profileImageUrl")!!
                    )

                    adapter.add(ChatFromItem(TextGetFromDoc!!, UserSend))
                }
            }
*/

     /*   Firebase.firestore.collection("user-messages")
            .whereEqualTo("senderDocId", receiverDId)
            .whereEqualTo("recieverDocId", senderDId)
           // .orderBy("timestamp")
            .get()
            .addOnSuccessListener { result ->
                for (document in result)
                    if (document.exists()) {
                        TextGetFromDoc = document.getString("text")


                        val UserReceive = User(
                            document.getString("uid")!!, document.getString("username")!!,
                            document.getString("profileImageUrl")!!
                        )

                        adapter.add(ChatToItem(TextGetFromDoc!!, UserReceive))
                    }
            }
*/

          val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")
       //   ref.keepSynced(true)
        ref.addChildEventListener(object : ChildEventListener {

             override fun onChildAdded(p0: DataSnapshot, p1: String?) {

                 val chatMessage = p0.getValue(ChatMessage::class.java)

                 if (chatMessage != null) {

                     if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
                         val currentUser = LatestMessagesActivity.currentUser ?:return
                         adapter.add(
                             ChatFromItem(
                                 chatMessage.text,
                                 currentUser,
                                 chatMessage.time
                             )
                         )

                     } else { adapter.add(
                         ChatToItem(
                             chatMessage.text,
                             toUser!!,
                             chatMessage.time
                         )
                     )
                     }
                 }
                    recyclerView_chat_log.scrollToPosition(adapter.itemCount-1)

             }


             override fun onCancelled(p0: DatabaseError) {

             }

             override fun onChildChanged(p0: DataSnapshot, p1: String?) {

             }

             override fun onChildMoved(p0: DataSnapshot, p1: String?) {

             }

             override fun onChildRemoved(p0: DataSnapshot) {

             }
         })
    }


    private fun performsendmessage() {
        val text = edittext_chat_log.text.toString()

        if(text.isEmpty())return

        val fromId = FirebaseAuth.getInstance().uid

        val toId = toUser?.uid

        if (fromId == null) return


/*     val Sendmessagedata = hashMapOf(
            "text" to text,
            "fromId" to fromId,
            "toId" to toId,
           // "senderDocId" to senderDocId,
           // "recieverDocId" to recieverDocId,
            "timestamp" to System.currentTimeMillis() / 1000
        )

        val Receivedmessagedata = hashMapOf(
            "text" to text,
            "fromId" to toId,
            "toId" to fromId,
           // "senderDocId" to recieverDocId,
           // "recieverDocId" to senderDocId,
            "timestamp" to System.currentTimeMillis() / 1000
        )

        Firebase.firestore.collection("user-messages").document(fromId).collection(toId!!)
            .add(Sendmessagedata)
            .addOnSuccessListener {
                edittext_chat_log.text.clear()
                recyclerView_chat_log.scrollToPosition(adapter.itemCount - 1) }

        Firebase.firestore.collection("user-messages").document(toId).collection(fromId)
            .add(Receivedmessagedata)
*/

   /*     Firebase.firestore.collection("user-messages").document().collection(fromId)
            .whereEqualTo("fromId", fromId)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.exists())
                        senderDocId = document.id

                }
            }

        Firebase.firestore.collection("user-messages").document().collection(toId)
            .whereEqualTo("toId", toId)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.exists())
                        recieverDocId = document.id

                }
            }
*/


 /*       val docref: DocumentReference =
            FirebaseFirestore.getInstance().collection("user-messages").document()

        val map1 = HashMap<String,Any>()
        map1.put("senderDocId",)
        docref.update(map1)
*/

/*
        Firebase.firestore.collection("latest-messages").document(fromId).collection(toId)
            .add(Sendmessagedata)

        Firebase.firestore.collection("latest-messages").document(toId).collection(fromId)
            .add(Receivedmessagedata)
*/

        val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()

        val toReference = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()

        val chatMessage = ChatMessage(
            reference.key!!, text, fromId, toId!!, System.currentTimeMillis() / 1000, NameOfDay!!
            , datetime!!, datetime2!!
        )

        reference.setValue(chatMessage).addOnSuccessListener {
            edittext_chat_log.text.clear()
            recyclerView_chat_log.scrollToPosition(adapter.itemCount-1)
        }

        toReference.setValue(chatMessage)

        val latestMessageRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
        latestMessageRef.setValue(chatMessage)

        val latestMessageToRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")
        latestMessageToRef.setValue(chatMessage)

    }




    class ChatFromItem(val text: String, val user: User, val time : String) : Item<GroupieViewHolder>() {
        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
         viewHolder.itemView.textView_from_row.text = text

         viewHolder.itemView.sent_at_time.text = time


            val uri = user.profileImageUrl
            val targetimageview = viewHolder.itemView.imageview_from_row

            Picasso.get().load(uri).networkPolicy(NetworkPolicy.OFFLINE).into(targetimageview,object : Callback{
                override fun onSuccess() {
                }

                override fun onError(e: Exception?) {
                    Picasso.get().load(uri).into(targetimageview)
                }

            })

        }
        override fun getLayout(): Int {
            return R.layout.chat_from_row
        }
    }

    class ChatToItem(val text: String, val toUser: User, val time2 : String ) : Item<GroupieViewHolder>() {
        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textView_to_row.text = text

        viewHolder.itemView.received_at_time.text = time2

        val uri = toUser.profileImageUrl
        val targetimageview = viewHolder.itemView.imageview_to_row
        Picasso.get().load(uri).into(targetimageview)
        }
        override fun getLayout(): Int {
            return R.layout.chat_to_row
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.select_color_green -> {
                recyclerView_chat_log.setBackgroundColor(Color.parseColor("#1D7804"))
            }
            R.id.select_color_red -> {
             recyclerView_chat_log.setBackgroundColor(Color.parseColor("#B80B0B"))
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_chatlog, menu)
        return super.onCreateOptionsMenu(menu)
    }

}

