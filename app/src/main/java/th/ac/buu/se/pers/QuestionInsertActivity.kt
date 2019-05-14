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

    var question_ref: DatabaseReference? = null
    var questionListener: ValueEventListener? = null

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
        val isInsert: Boolean = getIntent.getBooleanExtra("isInsert", true)
        var question_id: String? = null
        val subject_id: String? = getIntent.getStringExtra("subject_id")

        val mReference = mDatabase!!.reference

        val questionText = findViewById<EditText>(R.id.questionText)
        val correctText = findViewById<EditText>(R.id.correct_text)
        val incorrectText1 = findViewById<EditText>(R.id.incorrect_text_1)
        val incorrectText2 = findViewById<EditText>(R.id.incorrect_text_2)
        val incorrectText3 = findViewById<EditText>(R.id.incorrect_text_3)
        val saveBtn = findViewById<Button>(R.id.save_question_btn)

        if(!isInsert){

            actionbar.title = "Edit Question"
            question_id = getIntent.getStringExtra("question_id")
            question_ref = mDatabase!!.reference.child(COLLECTION_QUESTION).child(mAuth!!.currentUser!!.uid).child(subject_id!!).child(question_id!!).ref

            questionListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get Post object and use the values to update the UI
                    val question:String? = dataSnapshot.child("text").getValue<String>(String::class.java)
                    //set actionbar title
                    questionText.setText(question!!)

                    val choice1:String? = dataSnapshot.child("choices").child("0").child("text").getValue<String>(String::class.java)
                    val choice2:String? = dataSnapshot.child("choices").child("1").child("text").getValue<String>(String::class.java)
                    val choice3:String? = dataSnapshot.child("choices").child("2").child("text").getValue<String>(String::class.java)
                    val choice4:String? = dataSnapshot.child("choices").child("3").child("text").getValue<String>(String::class.java)

                    correctText.setText(choice1!!)
                    incorrectText1.setText(choice2!!)
                    incorrectText2.setText(choice3!!)
                    incorrectText3.setText(choice4!!)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w("FIREBASENAJA", "loadPost:onCancelled", databaseError.toException())
                    // ...
                }
            }


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

                    val update_ref = mReference.child(COLLECTION_QUESTION).child(mAuth!!.currentUser!!.uid).child(subject_id!!).child(question_id!!).ref

                    update_ref.child("choices").child("0").child("text").setValue(correctText.text.toString())
                    update_ref.child("choices").child("1").child("text").setValue(incorrectText1.text.toString())
                    update_ref.child("choices").child("2").child("text").setValue(incorrectText2.text.toString())
                    update_ref.child("choices").child("3").child("text").setValue(incorrectText3.text.toString())

                    update_ref.child("text").setValue(questionText.text.toString()).addOnSuccessListener {
                        Toast.makeText(this.applicationContext, "Updated", Toast.LENGTH_SHORT).show()

                        finish()
                    }

                }

            }
        }
    }

    override fun onStart() {
        super.onStart()
        mAuth!!.addAuthStateListener(mAuthListener!!)
        if(question_ref != null) {
            question_ref!!.addValueEventListener(questionListener!!)
        }
    }

    override fun onStop() {
        super.onStop()
        if(question_ref != null) {
            question_ref!!.removeEventListener(questionListener!!)
        }
        if (mAuthListener != null) {
            mAuth!!.removeAuthStateListener(mAuthListener!!)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
