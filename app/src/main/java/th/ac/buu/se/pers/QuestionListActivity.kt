package th.ac.buu.se.pers

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_question_list.*
import android.support.design.widget.BottomSheetDialog
import android.support.v7.app.AlertDialog


class QuestionListActivity : AppCompatActivity() {

    var COLLECTION_SUBJECTS = "subjects"
    var COLLECTION_QUESTION = "question"

    var mAuth: FirebaseAuth? = FirebaseAuth.getInstance()
    var mAuthListener: FirebaseAuth.AuthStateListener? = null
    var mDatabase: FirebaseDatabase? = FirebaseDatabase.getInstance()
    var mFirebaseAdapter: FirebaseRecyclerAdapter<QuestionData, QuestionViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_list)

        //actionbar
        val actionbar = supportActionBar

        var getIntent = intent
        val subject_id: String? = getIntent.getStringExtra("subject_id")
        val subject_ref: DatabaseReference = mDatabase!!.reference.child(COLLECTION_SUBJECTS).child(subject_id!!).ref
        var subject: SubjectData?

        val subjectListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                subject = dataSnapshot.getValue<SubjectData>(SubjectData::class.java)
                //set actionbar title
                actionbar!!.title = "Question Lists of " + subject!!.name
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("FIREBASENAJA", "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        }
        subject_ref.addValueEventListener(subjectListener)

        //set back button
        actionbar!!.setDisplayHomeAsUpEnabled(true)

        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {
                finish()
            }
        }

        // get element in layout
        var fab = findViewById<FloatingActionButton>(R.id.btn_fab_add)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_question_lists)
        recyclerView.layoutManager = LinearLayoutManager(this.applicationContext)

        // button add product
        fab.setOnClickListener {
            var intent = Intent(this, QuestionInsertActivity::class.java)
            intent.putExtra("isInsert", true)
            intent.putExtra("subject_id", subject_id)
            startActivity(intent)
        }

        val mReference = mDatabase!!.reference

        val query: Query = mReference.child(COLLECTION_QUESTION).child(mAuth!!.currentUser!!.uid).child(subject_id)
        val options = FirebaseRecyclerOptions.Builder<QuestionData>()
            .setQuery(query, QuestionData::class.java)
            .build()

        mFirebaseAdapter = object : FirebaseRecyclerAdapter<QuestionData, QuestionViewHolder>(options) {
            override fun onCreateViewHolder(p0: ViewGroup, p1: Int): QuestionViewHolder {
                val inflater = LayoutInflater.from(p0.context)
                return QuestionViewHolder(inflater.inflate(R.layout.item_question, p0, false))
            }

            override fun onBindViewHolder(holder: QuestionViewHolder, position: Int, model: QuestionData) {
                holder.name.text = model.text

                holder.itemView.setOnClickListener {
                    Toast.makeText(it.context, model.text, Toast.LENGTH_LONG).show()

                    val intent = Intent(it.context, QuestionActivity::class.java)
                    intent.putExtra("subject_id", subject_id)
                    intent.putExtra("question_id", model.id)
                    startActivity(intent)
                }

                holder.moreBtn.setOnClickListener {

                    val inflater = LayoutInflater.from(holder.itemView.context)
                    val bottomSheetView = inflater.inflate(R.layout.toolbar_question, null)
                    val bottomSheetDialog = BottomSheetDialog(holder.itemView.context)
                    bottomSheetDialog.setContentView(bottomSheetView)

                    bottomSheetDialog.show()

                    val questionText: TextView = bottomSheetView.findViewById(R.id.toolbar_qt_name)
                    val editBtn: Button = bottomSheetView.findViewById(R.id.question_menu_bottom_sheet_edit)
                    val removeBtn: Button = bottomSheetView.findViewById(R.id.question_menu_bottom_sheet_delete)

                    questionText.text = model.text

                    editBtn.setOnClickListener {
                        Toast.makeText(it.context, "edit", Toast.LENGTH_SHORT).show()

                        bottomSheetDialog.hide()

                        val intent = Intent(bottomSheetView.context, QuestionInsertActivity::class.java)
                        intent.putExtra("isInsert", false)
                        intent.putExtra("subject_id", subject_id)
                        intent.putExtra("question_id", model.id)
                        startActivity(intent)
                    }

                    removeBtn.setOnClickListener {

                        val builder = AlertDialog.Builder(bottomSheetView.context)
                        builder.setTitle("Delete Question")
                        builder.setMessage("Are you sure to delete this question?")

                        builder.setPositiveButton("YES"){dialog, which ->
                            mReference.child(COLLECTION_QUESTION).child(mAuth!!.currentUser!!.uid).child(subject_id).child(model.id.toString()).removeValue()
                            Toast.makeText(it.context, "deleted!", Toast.LENGTH_SHORT).show()
                            bottomSheetDialog.hide()
                        }

                        builder.setNeutralButton("Cancel"){_,_ ->
                            Toast.makeText(applicationContext,"You cancelled the dialog.",Toast.LENGTH_SHORT).show()
                        }

                        val dialog: AlertDialog = builder.create()

                        dialog.show()
                    }
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

    class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.question_item_text)
        var moreBtn: Button = itemView.findViewById(R.id.more_btn)
    }
}