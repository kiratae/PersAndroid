package th.ac.buu.se.pers

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

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
        val isInsert: Boolean? = getIntent.getBooleanExtra("isInsert", true)
        var question_id: String? = null
        val subject_id: String? = getIntent.getStringExtra("subject_id")

        val mReference = mDatabase!!.reference

        val questionText = findViewById<EditText>(R.id.questionText)
        val correctText = findViewById<EditText>(R.id.correct_text)
        val incorrectText1 = findViewById<EditText>(R.id.incorrect_text_1)
        val incorrectText2 = findViewById<EditText>(R.id.incorrect_text_2)
        val incorrectText3 = findViewById<EditText>(R.id.incorrect_text_3)
        val saveBtn = findViewById<Button>(R.id.save_question_btn)

        if(isInsert != true){

            actionbar.title = "Edit Question"
            question_id = getIntent.getStringExtra("question_id")
            val question_ref: DatabaseReference = mDatabase!!.reference.child(COLLECTION_QUESTION).child(mAuth!!.currentUser!!.uid).child(subject_id!!).child(question_id!!).ref
            var question: QuestionData?
            var choice1: QuestionData.ChoicesData?
            var choice2: QuestionData.ChoicesData?
            var choice3: QuestionData.ChoicesData?
            var choice4: QuestionData.ChoicesData?

            val questionListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get Post object and use the values to update the UI
                    question = dataSnapshot.getValue<QuestionData>(QuestionData::class.java)
                    //set actionbar title
                    questionText.setText(question!!.text)

                    choice1 = dataSnapshot.child("choices").child("0").getValue<QuestionData.ChoicesData>(QuestionData.ChoicesData::class.java)
                    choice2 = dataSnapshot.child("choices").child("1").getValue<QuestionData.ChoicesData>(QuestionData.ChoicesData::class.java)
                    choice3 = dataSnapshot.child("choices").child("2").getValue<QuestionData.ChoicesData>(QuestionData.ChoicesData::class.java)
                    choice4 = dataSnapshot.child("choices").child("3").getValue<QuestionData.ChoicesData>(QuestionData.ChoicesData::class.java)

                    correctText.setText(choice1!!.text)
                    incorrectText1.setText(choice2!!.text)
                    incorrectText2.setText(choice3!!.text)
                    incorrectText3.setText(choice4!!.text)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w("FIREBASENAJA", "loadPost:onCancelled", databaseError.toException())
                    // ...
                }
            }
            question_ref.addValueEventListener(questionListener)
        }

        saveBtn.setOnClickListener {
            if(questionText == null){
                Toast.makeText(this.applicationContext, "Question Text is Empty!", Toast.LENGTH_SHORT).show()
            }else {

                if(isInsert == true){
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

                        Toast.makeText(this.applicationContext, "Saved", Toast.LENGTH_SHORT).show()

                        finish()

                    }
                }else{

                    mReference.child(COLLECTION_QUESTION).child(mAuth!!.currentUser!!.uid).child(subject_id!!).child(question_id!!).child("text").setValue(questionText.text.toString())

                    val choicesData1 = QuestionData.ChoicesData(correctText.text.toString(),"correct")
                    val choicesData2 = QuestionData.ChoicesData(incorrectText1.text.toString(),"incorrect")
                    val choicesData3 = QuestionData.ChoicesData(incorrectText2.text.toString(),"incorrect")
                    val choicesData4 = QuestionData.ChoicesData(incorrectText3.text.toString(),"incorrect")

                    mReference.child(COLLECTION_QUESTION).child(mAuth!!.currentUser!!.uid).child(subject_id).child(question_id).child("choices").child("0").setValue(choicesData1)
                    mReference.child(COLLECTION_QUESTION).child(mAuth!!.currentUser!!.uid).child(subject_id).child(question_id).child("choices").child("1").setValue(choicesData2)
                    mReference.child(COLLECTION_QUESTION).child(mAuth!!.currentUser!!.uid).child(subject_id).child(question_id).child("choices").child("2").setValue(choicesData3)
                    mReference.child(COLLECTION_QUESTION).child(mAuth!!.currentUser!!.uid).child(subject_id).child(question_id).child("choices").child("3").setValue(choicesData4)

                    Toast.makeText(this.applicationContext, "Updated", Toast.LENGTH_SHORT).show()

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
