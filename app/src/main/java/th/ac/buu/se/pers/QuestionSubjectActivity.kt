package th.ac.buu.se.pers

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.ActionBar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query

class QuestionSubjectActivity : AppCompatActivity() {

    var COLLECTION_SUBJECTS = "subjects"

    var mAuth: FirebaseAuth? = FirebaseAuth.getInstance()
    var mAuthListener: FirebaseAuth.AuthStateListener? = null
    var mDatabase: FirebaseDatabase? = FirebaseDatabase.getInstance()
    var mFirebaseAdapter: FirebaseRecyclerAdapter<SubjectData, SubjectViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_subject)

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Question Subject"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)

        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {
                finish()
            }
        }

        // get element in layout
        var fab = findViewById<FloatingActionButton>(R.id.btn_fab_add)

        // button add product
        fab.setOnClickListener {
            var intent = Intent(this, SubjectInsertActivity::class.java)
            startActivity(intent)
        }

        //question_subject_recycler
        val recyclerView = findViewById<RecyclerView>(R.id.question_subject_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this.applicationContext)

        val mReference = mDatabase!!.reference

//        mReference.child(COLLECTION_SUBJECTS).setValue(
//            SubjectData(mReference.key.toString(), "ท-0123", "ภาษาไทย", "1/2561")
//        )

//        val subjectData = SubjectData(mReference.key.toString(), "ท-0123", "ภาษาไทย", "1/2561")
//
//        mReference.setValue(subjectData)

        val query: Query = mReference.child(COLLECTION_SUBJECTS)
        val options = FirebaseRecyclerOptions.Builder<SubjectData>()
            .setQuery(query, SubjectData::class.java)
            .build()

        mFirebaseAdapter = object : FirebaseRecyclerAdapter<SubjectData, SubjectViewHolder>(options) {
            override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SubjectViewHolder {
                val inflater = LayoutInflater.from(p0.context)
                return SubjectViewHolder(inflater.inflate(R.layout.item_subject, p0, false))
            }

            override fun onBindViewHolder(holder: SubjectViewHolder, position: Int, model: SubjectData) {
                holder.code.text = model.code
                holder.name.text = model.name
                holder.year.text = model.year

                holder.itemView.setOnClickListener {
                    Toast.makeText(it.context, model.code+" "+model.name, Toast.LENGTH_LONG).show()

                    val intent = Intent(it.context, QuestionListActivity::class.java)
                    intent.putExtra("subject_id", model.id)
                    startActivity(intent)
                }
            }

        }

        recyclerView.adapter = mFirebaseAdapter
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

    class SubjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var code: TextView = itemView.findViewById(R.id.show_code)
        var name: TextView = itemView.findViewById(R.id.show_name)
        var year: TextView = itemView.findViewById(R.id.show_year)

    }
}
