package th.ac.buu.se.pers

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query


class ChatActivity : AppCompatActivity() {

    var COLLECTION_QUESTION = "question"

    var mAuth: FirebaseAuth? = FirebaseAuth.getInstance()
    var mAuthListener: FirebaseAuth.AuthStateListener? = null
    var mDatabase: FirebaseDatabase? = FirebaseDatabase.getInstance()
    var mFirebaseAdapter: FirebaseRecyclerAdapter<QuestionData, MessageViewHolder>? = null

    var noLoginUserText = "ANONYMOUS"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Chat Activity"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        var btn_send = findViewById<Button>(R.id.btn_send)
        var btn_logout = findViewById<Button>(R.id.btn_logout)


        var mReference = mDatabase!!.reference

        btn_send.setOnClickListener {
            var input_text = findViewById<EditText>(R.id.input_text)
            //var messagerText = mAuth!!.currentUser!!.email.toString()
            var messageText = input_text.text.toString()
            if (messageText != "") {
//                mReference.child("question").push().setValue(
//                    QuestionData(messageText, "1")
//                )
            } else {
                Toast.makeText(this, "No Message ?", Toast.LENGTH_LONG).show()
            }
            input_text.setText("")
        }
        btn_logout.setOnClickListener {
            mAuth!!.signOut()
        }

        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {
//                finish()
            }

        }

        var list_chat = findViewById<RecyclerView>(R.id.list_chat)
        list_chat.layoutManager = LinearLayoutManager(this)

        var query: Query = mReference.child("question")
        var option = FirebaseRecyclerOptions.Builder<QuestionData>()
            .setQuery(query, QuestionData::class.java)
            .build()

        Log.i("FIREBASENAJA", query.toString())

        mFirebaseAdapter = object : FirebaseRecyclerAdapter<QuestionData, MessageViewHolder>(option){
            override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MessageViewHolder {
                val inflater = LayoutInflater.from(p0.context)
                return MessageViewHolder(inflater.inflate(R.layout.item_message, p0, false))
            }

            override fun onBindViewHolder(holder: MessageViewHolder, position: Int, model: QuestionData) {
                holder.messageTextView.text = model.text
//                holder.messengerTextView.text = model.status.toString()
            }

        }
        list_chat.adapter = mFirebaseAdapter


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onStart() {
        super.onStart()
        mAuth!!.addAuthStateListener(mAuthListener!!)
        if (mFirebaseAdapter != null) {
            mFirebaseAdapter!!.startListening()
        }
    }

    override fun onStop() {
        super.onStop()
        if (mFirebaseAdapter != null) {
            mFirebaseAdapter!!.stopListening()
        }
    }

    class MessageViewHolder internal constructor(v: View) : RecyclerView.ViewHolder(v) {
        internal var row: LinearLayout
        internal var messageTextView: TextView
        internal var messengerTextView: TextView

        init {
            row = itemView.findViewById(R.id.row)
            messageTextView = itemView.findViewById(R.id.show_name)
            messengerTextView = itemView.findViewById(R.id.show_name)
        }
    }

}

