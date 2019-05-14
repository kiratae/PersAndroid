package th.ac.buu.se.pers

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class QuestionInsertActivity : AppCompatActivity() {

    var COLLECTION_QUESTION = "question"

    var mAuth: FirebaseAuth? = FirebaseAuth.getInstance()
    var mAuthListener: FirebaseAuth.AuthStateListener? = null
    var mDatabase: FirebaseDatabase? = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_insert)

        //actionbar
        val actionbar = supportActionBar
        actionbar!!.title = "Insert Question"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)

        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {
                finish()
            }
        }

        val getIntent = intent
        val subject_id: String? = getIntent.getStringExtra("subject_id")

        val mReference = mDatabase!!.reference

        val questionText = findViewById<EditText>(R.id.questionText)
        val correctText = findViewById<EditText>(R.id.correct_text)
        val incorrectText1 = findViewById<EditText>(R.id.incorrect_text_1)
        val incorrectText2 = findViewById<EditText>(R.id.incorrect_text_2)
        val incorrectText3 = findViewById<EditText>(R.id.incorrect_text_3)
        val saveBtn = findViewById<Button>(R.id.save_question_btn)

        saveBtn.setOnClickListener {
            if(questionText == null){
                Toast.makeText(this.applicationContext, "Question Text is Empty!", Toast.LENGTH_SHORT).show()
            }else {
                Toast.makeText(this.applicationContext, "Save & Next", Toast.LENGTH_SHORT).show()

                val key: String? = mReference.child(COLLECTION_QUESTION).child(mAuth!!.currentUser!!.uid).child(subject_id!!).push().key

                val questionData = QuestionData(
                    key!!,
                    questionText.text.toString()
                )

                mReference.child(COLLECTION_QUESTION).child(mAuth!!.currentUser!!.uid).child(subject_id).child(key).setValue(questionData).addOnCompleteListener {

                    val choicesData1 = QuestionData.ChoicesData(correctText.text.toString(),"correct")
                    val choicesData2 = QuestionData.ChoicesData(incorrectText1.text.toString(),"incorrect")
                    val choicesData3 = QuestionData.ChoicesData(incorrectText2.text.toString(),"incorrect")
                    val choicesData4 = QuestionData.ChoicesData(incorrectText3.text.toString(),"incorrect")

                    mReference.child(COLLECTION_QUESTION).child(mAuth!!.currentUser!!.uid).child(subject_id).child(key).child("choices").child("0").setValue(choicesData1)
                    mReference.child(COLLECTION_QUESTION).child(mAuth!!.currentUser!!.uid).child(subject_id).child(key).child("choices").child("1").setValue(choicesData2)
                    mReference.child(COLLECTION_QUESTION).child(mAuth!!.currentUser!!.uid).child(subject_id).child(key).child("choices").child("2").setValue(choicesData3)
                    mReference.child(COLLECTION_QUESTION).child(mAuth!!.currentUser!!.uid).child(subject_id).child(key).child("choices").child("3").setValue(choicesData4)

                    finish()

                }
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
        onBackPressed()
        return true
    }
}
