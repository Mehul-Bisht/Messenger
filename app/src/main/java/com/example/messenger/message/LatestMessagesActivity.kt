package com.example.messenger.message

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import com.example.messenger.R
import com.example.messenger.all_settings.SettingsActivity
import com.example.messenger.all_settings.UserProfileActivity
import com.example.messenger.models.ChatMessage
import com.example.messenger.models.User
import com.example.messenger.splashscreen.IntroActivity
import com.example.messenger.register_login.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_latest_messages.*
import kotlinx.android.synthetic.main.latest_messages_row.view.*
import java.lang.Exception

class LatestMessagesActivity : AppCompatActivity() {

    companion object{
     var currentUser : User? = null
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)

        val actionbar : ActionBar = supportActionBar!!
        actionbar.setDisplayShowHomeEnabled(true)
        actionbar.setIcon(R.mipmap.ic_launcher)

        recyclerview_latest_messages.adapter = adapter
    //    recyclerview_latest_messages.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))


        adapter.setOnItemClickListener{item, view ->

            val intent = Intent(this, ChatLogActivity::class.java)
            val row = item as LatestMessageRow
            intent.putExtra(NewMessageActivity.USER_KEY, row.chatPartnerUser)
           // intent.putExtra("OKEY", currentUser)
            startActivity(intent)
        }

        verifyUserIsLoggedIn()

        fetchCurrentUser()

        listenForLatestMessages()

    }
    val adapter = GroupAdapter<GroupieViewHolder>()

    val latestMesaagesMap = HashMap<String , ChatMessage>()

    private fun RefreshRecyclerViewMessages(){
        adapter.clear()
        latestMesaagesMap.values.forEach{
            adapter.add(
                LatestMessageRow(
                    it
                )
            )
        }
    }


    class LatestMessageRow(val chatMessage: ChatMessage): Item<GroupieViewHolder>() {

        var chatPartnerUser: User? = null

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {

            val trimmed = ""
            val latestmessagetext = chatMessage.text

           /* if(latestmessagetext.length > viewHolder.itemView.message_textview_latestmessage.length())
            { trimmed = latestmessagetext.trimSubstring(0,
                viewHolder.itemView.message_textview_latestmessage.length() - 3) + "..." }
            else
                trimmed = chatMessage.text
            */
           /* if(latestmessagetext.length > 35)
            {   trimmed = latestmessagetext.trimSubstring(0,35) + "..." }

            else
            {   trimmed = chatMessage.text } */

            val Date = chatMessage.date

            viewHolder.itemView.date.text = Date

            viewHolder.itemView.message_textview_latestmessage.text = latestmessagetext

            val dateandtime = chatMessage.day +" , "+ chatMessage.time
            viewHolder.itemView.daydate.text = dateandtime

            val chatPartnerUserId = chatMessage.fromId

          /*  Firebase.firestore.collection("latest-messages").document(chatPartnerUserId).collection(chatMessage.toId)
                //.whereEqualTo("fromId",chatPartnerUserId)
                //.whereEqualTo("toId",FirebaseAuth.getInstance().uid)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result)
                        if (document.exists()) {
                            chatPartnerUser = User(document.getString("uid")!!,
                                document.getString("username")!!,
                                document.getString("profileImageUrl")!!)
                        }
                }
            */

            val targetimageview = viewHolder.itemView.imageView
            Picasso.get().load(chatPartnerUser?.profileImageUrl).networkPolicy(NetworkPolicy.OFFLINE).into(targetimageview, object : Callback {
                override fun onSuccess() {

                }

                override fun onError(e: Exception?) {
                    Picasso.get().load(chatPartnerUser?.profileImageUrl).into(targetimageview)
                }
            })
            viewHolder.itemView.username_textView_latestmessage.text =
                chatPartnerUser?.username




                         val chatPartnerId: String
                          if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
                              chatPartnerId = chatMessage.toId
                          } else
                              chatPartnerId = chatMessage.fromId


                 val ref = FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")
            ref.keepSynced(true)
              ref.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(p0: DataSnapshot) {
                    chatPartnerUser = p0.getValue(User::class.java)

                  //  val targetimageview = viewHolder.itemView.imageView
                  //  Picasso.get().load(chatPartnerUser?.profileImageUrl).into(targetimageview)
                  //  viewHolder.itemView.username_textView_latestmessage.text = chatPartnerUser?.username

                    val targetimageview = viewHolder.itemView.imageView
                    Picasso.get().load(chatPartnerUser?.profileImageUrl).networkPolicy(NetworkPolicy.OFFLINE).into(targetimageview, object : Callback{
                        override fun onSuccess() {

                        }

                        override fun onError(e: Exception?) {
                            Picasso.get().load(chatPartnerUser?.profileImageUrl).placeholder(
                                R.drawable.default_user
                            )
                                .error(R.drawable.default_user)
                                .into(targetimageview)
                        }
                    })

                    viewHolder.itemView.username_textView_latestmessage.text =
                        chatPartnerUser?.username

                }
                override fun onCancelled(p0: DatabaseError) {

                }
            })


        }

            override fun getLayout(): Int {
                return R.layout.latest_messages_row
            }
        }



    private fun listenForLatestMessages(){
     val fromId = FirebaseAuth.getInstance().uid

 /*     Firebase.firestore.collection("latest-messages").document().collection(fromId!!)
          .whereEqualTo("toId",fromId)
        //  .orderBy("timestamp")
          .get()
          .addOnSuccessListener { result->
              for(document in result)
                  if(document.exists()){
                    val chatmessage = ChatMessage(document.getString("text")!!,
                        document.getString("fromId")!!,
                        document.getString("toId")!!,
                        document.getLong("timestamp")!!
                      )

                     adapter.add(LatestMessageRow(chatmessage))
                      RefreshRecyclerViewMessages()
                  }
          }
*/

     val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")
        //ref.keepSynced(true)
        ref.addChildEventListener(object : ChildEventListener{

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
           val chatMessage = p0.getValue(ChatMessage::class.java) ?:return

            latestMesaagesMap[p0.key!!] = chatMessage

                adapter.add(
                    LatestMessageRow(
                        chatMessage
                    )
                )
                RefreshRecyclerViewMessages()
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java) ?:return

                latestMesaagesMap[p0.key!!] = chatMessage

                adapter.add(
                    LatestMessageRow(
                        chatMessage
                    )
                )
                RefreshRecyclerViewMessages()
            }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }
            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onChildRemoved(p0: DataSnapshot) {

            }
        })

    }

  //  val adapter = GroupAdapter<GroupieViewHolder>()



    private fun fetchCurrentUser(){
        val uid = FirebaseAuth.getInstance().uid
        /*Firebase.firestore.collection("users")
            .whereEqualTo("uid",uid)
            .get()
        */

         val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
         ref.addListenerForSingleValueEvent(object: ValueEventListener{

             override fun onDataChange(p0: DataSnapshot) {
             currentUser = p0.getValue(User::class.java)

             }

             override fun onCancelled(p0: DatabaseError) {

             }
         } )

    }



    private fun verifyUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            val intent = Intent(this, IntroActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_new_message -> {

                val intent = Intent(this, NewMessageActivity::class.java)
                startActivity(intent)
            }

            R.id.menu_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }

            R.id.Myaccount ->{
                val intent = Intent(this,
                    UserProfileActivity::class.java)
                //intent.putExtra("KEY", currentUser)
                startActivity(intent)
            }

            R.id.select_color -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    }

