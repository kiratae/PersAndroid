package th.ac.buu.se.pers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

        //question_subject_recycler
        var recyclerView = findViewById<RecyclerView>(R.id.question_subject_recycler)

        var mReference = mDatabase!!.reference

        mReference.child(COLLECTION_SUBJECTS).push().setValue(
            SubjectData("พล-1234", "พละศึกษา", "2561")
        )


        var query: Query = mReference.child(COLLECTION_SUBJECTS)
        var options = FirebaseRecyclerOptions.Builder<SubjectData>()
            .setQuery(query, SubjectData::class.java)
            .build()

        Log.i("FIREBASENAJA", query.toString())

        mFirebaseAdapter = object : FirebaseRecyclerAdapter<SubjectData, SubjectViewHolder>(options) {
            override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SubjectViewHolder {
                val inflater = LayoutInflater.from(p0.context)
                return SubjectViewHolder(inflater.inflate(R.layout.item_message, p0, false))
            }

            override fun onBindViewHolder(holder: SubjectViewHolder, position: Int, model: SubjectData) {
                holder.show_code.text = model.code
                holder.show_name.text = model.name
//                holder.show_year.text = model.year
                Log.i("FIREBASENAJA", model.code)
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
        var show_code: TextView
        var show_name: TextView
//        var show_year: TextView

        init {
            show_code = itemView.findViewById(R.id.show_m)
            show_name = itemView.findViewById(R.id.show_mm)
//            show_year = itemView.findViewById(R.id.show_year)
        }

    }
}
