package th.ac.buu.se.pers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class QuestionActivity : AppCompatActivity() {

    var COLLECTION_QUESTION = "question"

    var question_ref: DatabaseReference? = null
    var questionListener: ValueEventListener? = null

    var mAuth: FirebaseAuth? = FirebaseAuth.getInstance()
    var mAuthListener: FirebaseAuth.AuthStateListener? = null
    var mDatabase: FirebaseDatabase? = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_show)

        //actionbar
        val actionbar = supportActionBar
        actionbar!!.title = "Question"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)

        val getIntent = intent
        val subject_id: String? = getIntent.getStringExtra("subject_id")
        val question_id: String? = getIntent.getStringExtra("question_id")
        question_ref = mDatabase!!.reference.child(COLLECTION_QUESTION).child(mAuth!!.currentUser!!.uid).child(subject_id!!).child(question_id!!).ref
        var question: QuestionData?
        var choice1: QuestionData.ChoicesData?
        var choice2: QuestionData.ChoicesData?
        var choice3: QuestionData.ChoicesData?
        var choice4: QuestionData.ChoicesData?

        val questionText = findViewById<TextView>(R.id.question_txv)
        val choiceText1 = findViewById<TextView>(R.id.choice_txv_1)
        val choiceText2 = findViewById<TextView>(R.id.choice_txv_2)
        val choiceText3 = findViewById<TextView>(R.id.choice_txv_3)
        val choiceText4 = findViewById<TextView>(R.id.choice_txv_4)

        questionListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                question = dataSnapshot.getValue<QuestionData>(QuestionData::class.java)
                //set actionbar title
                questionText.text = question!!.text

                choice1 = dataSnapshot.child("choices").child("0").getValue<QuestionData.ChoicesData>(QuestionData.ChoicesData::class.java)
                choice2 = dataSnapshot.child("choices").child("1").getValue<QuestionData.ChoicesData>(QuestionData.ChoicesData::class.java)
                choice3 = dataSnapshot.child("choices").child("2").getValue<QuestionData.ChoicesData>(QuestionData.ChoicesData::class.java)
                choice4 = dataSnapshot.child("choices").child("3").getValue<QuestionData.ChoicesData>(QuestionData.ChoicesData::class.java)

                choiceText1.text = choice1!!.text
                choiceText2.text = choice2!!.text
                choiceText3.text = choice3!!.text
                choiceText4.text = choice4!!.text
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("FIREBASENAJA", "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        }
        question_ref!!.addValueEventListener(questionListener!!)

        Toast.makeText(this.applicationContext, question_id, Toast.LENGTH_LONG).show()

        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {
                finish()
            }
        }


    }

    override fun onStart() {
        super.onStart()
        mAuth!!.addAuthStateListener(mAuthListener!!)
    }

    override fun onStop() {
        super.onStop()
        if (mAuthListener != null) {
            mAuth!!.removeAuthStateListener(mAuthListener!!)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        question_ref!!.removeEventListener(questionListener!!)
        onBackPressed()
        return true
    }
}
