package com.example.messenger.message

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.messenger.R
import com.example.messenger.models.User
import com.example.messenger.models.UserStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class NewMessageActivity : AppCompatActivity() {

    companion object{
        val USER_KEY = "USER_KEY"
    }

    //var adapter
    val adapter = GroupAdapter<GroupieViewHolder>()

    var newmessageuserstatusref : DatabaseReference? = null

    var newmessagestatus : UserStatus? = null

    var newmessagestatus2 : UserStatus? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title = "Select or seach for users"
        val actionBar : ActionBar = supportActionBar!!
        actionBar.setDisplayHomeAsUpEnabled(true)
        
        fetchUsers()


         recyclerview_newmessage.adapter = adapter
         recyclerview_newmessage.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL ))
    }



    private fun fetchUsers(){


        val uidtobeused = LatestMessagesActivity.currentUser?.uid

        newmessageuserstatusref =
            FirebaseDatabase.getInstance().getReference("/user-active-status/$uidtobeused")

        newmessageuserstatusref?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                newmessagestatus = p0.getValue(UserStatus::class.java)

            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })

    //    val adapter = GroupAdapter<GroupieViewHolder>()

      /*  Firebase.firestore.collection("users")
            .get()
            .addOnSuccessListener {result->
                for(document in result)
                    if(document.exists()) {
                       val searchlist = User(document.getString("uid")!!,document.getString("username")!!,
                           document.getString("profileImageUrl")!!)
                           adapter.add(UserItem(searchlist))
                    }
            }
        */

        val ref = FirebaseDatabase.getInstance().getReference("/users")
      // ref.keepSynced(true)
    ref.addListenerForSingleValueEvent(object: ValueEventListener {

        override fun onDataChange(p0: DataSnapshot) {

      //  val adapter = GroupAdapter<GroupieViewHolder>()

        p0.children.forEach {
            val user = it.getValue(User::class.java)
            if (user != null && user.uid!=FirebaseAuth.getInstance().uid) {

                if(newmessagestatus!=null)
                    adapter.add(
                        UserItem(
                            user,
                            newmessagestatus!!
                        )
                    )
            }
        }


         adapter.setOnItemClickListener { item, view ->

             val userItem = item as UserItem
             val intent = Intent(view.context, ChatLogActivity::class.java)
             intent.putExtra(USER_KEY,userItem.user)
             startActivity(intent)

             finish()
         }

        // recyclerview_newmessage.adapter = adapter
        // recyclerview_newmessage.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL ))
}

        override fun onCancelled(p0: DatabaseError) {

        }

    })




    }

    class UserItem(val user: User, val userstatusobject : UserStatus): Item<GroupieViewHolder>(){
        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.username_textview_new_message.text = user.username
            viewHolder.itemView.status.text = userstatusobject.statusText
         //   viewHolder.itemView.status.text = user.status
            Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.imageView_new_message)

        }

        override fun getLayout(): Int {
            return R.layout.user_row_new_message
        }
    }

   /*

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    */

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_newmessage, menu)

        val searchItem = menu?.findItem(R.id.search)
        val searchView = searchItem?.actionView as SearchView
        searchView.queryHint = "Search by a username"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val adapter1 = GroupAdapter<GroupieViewHolder>()

                if(newText!!.isNotEmpty() && newText.length > 0){
                    adapter1.clear()
                val to_be_searched =  newText.toLowerCase()
                    val Ref = FirebaseDatabase.getInstance().getReference("/users")
                    //Ref.keepSynced(true)
                    val StatusReference = FirebaseDatabase.getInstance().getReference("/user-active-status")
                    Ref.addListenerForSingleValueEvent(object : ValueEventListener {

                        override fun onDataChange(p0: DataSnapshot) {


                            p0.children.forEach {
                                val user = it.getValue(User::class.java)
                                if (user != null && user.uid != FirebaseAuth.getInstance().uid) {
                                    StatusReference.child("/${user.uid}").addListenerForSingleValueEvent(object : ValueEventListener{
                                        override fun onCancelled(p0: DatabaseError) {

                                        }

                                        override fun onDataChange(p0: DataSnapshot) {
                                            newmessagestatus2 = p0.getValue(UserStatus::class.java)
                                        }

                                    })
                                    val searchvalue = user.username.toLowerCase()
                                    if(searchvalue.contains(to_be_searched))
                                        if(newmessagestatus2!=null)
                                    adapter1.add(
                                        UserItem(
                                            user,
                                            newmessagestatus2!!
                                        )
                                    )
                                }
                            }

                            adapter1.setOnItemClickListener { item, view ->
                                val userItem = item as UserItem
                                val intent = Intent(view.context, ChatLogActivity::class.java)
                                //intent.putExtra(USER_KEY,userItem.user)
                                //intent.putExtra(USER_KEY,userItem.user)
                                intent.putExtra(USER_KEY, userItem.user)
                                startActivity(intent)
                                finish()  }
                            recyclerview_newmessage.adapter = adapter1
                        }
                        override fun onCancelled(p0: DatabaseError) {}
                    })
                }

                else if(newText.isEmpty()){
                    adapter1.clear()
                  //  val to_be_searched = newText.toLowerCase()
                    val Ref = FirebaseDatabase.getInstance().getReference("/users")
                    Ref.addListenerForSingleValueEvent(object : ValueEventListener {

                        val StatusReference2 =
                            FirebaseDatabase.getInstance().getReference("/user-active-status")


                        override fun onDataChange(p0: DataSnapshot) {


                            p0.children.forEach {
                                val user = it.getValue(User::class.java)
                                if (user != null) {
                                  //  val searchvalue = user.username.toLowerCase()
                                   // if (searchvalue.contains(to_be_searched))
                                    StatusReference2.child("/${user.uid}")
                                        .addListenerForSingleValueEvent(object :
                                            ValueEventListener {
                                            override fun onCancelled(p0: DatabaseError) {

                                            }

                                            override fun onDataChange(p0: DataSnapshot) {
                                                newmessagestatus2 =
                                                    p0.getValue(UserStatus::class.java)
                                            }

                                        })
                                    if(newmessagestatus2!=null)
                                        adapter1.add(
                                            UserItem(
                                                user,
                                                newmessagestatus2!!
                                            )
                                        )
                                }
                            }

                            adapter1.setOnItemClickListener { item, view ->
                                val userItem = item as UserItem
                                val intent = Intent(view.context, ChatLogActivity::class.java)
                                //intent.putExtra(USER_KEY,userItem.user)
                                //intent.putExtra(USER_KEY,userItem.user)
                                intent.putExtra(USER_KEY, userItem.user)
                                startActivity(intent)
                                finish()
                            }
                            recyclerview_newmessage.adapter = adapter1
                        }

                        override fun onCancelled(p0: DatabaseError) {}
                    })
                }

                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

}

