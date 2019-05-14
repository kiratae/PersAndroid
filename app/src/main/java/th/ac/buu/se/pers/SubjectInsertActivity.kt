package th.ac.buu.se.pers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SubjectInsertActivity : AppCompatActivity() {

    var COLLECTION_SUBJECTS = "subjects"

    var mAuth: FirebaseAuth? = FirebaseAuth.getInstance()
    var mAuthListener: FirebaseAuth.AuthStateListener? = null
    var mDatabase: FirebaseDatabase? = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject_insert)

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Insert Subject"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)

        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {
                finish()
            }
        }

        val codeText = findViewById<EditText>(R.id.subject_code_edt)
        val nameText = findViewById<EditText>(R.id.subject_name_edt)
        val saveBtn = findViewById<Button>(R.id.save_subject_btn)

        val mReference = mDatabase!!.reference

        saveBtn.setOnClickListener {

            val key: String? = mReference.child(COLLECTION_SUBJECTS).push().key

            val subjectData = SubjectData(
                key!!,
                codeText.text.toString(),
                nameText.text.toString(),
                "1/2561"
            )

            mReference.child(COLLECTION_SUBJECTS).child(key).setValue(subjectData).addOnCompleteListener {

                Toast.makeText(this.applicationContext, "Saved", Toast.LENGTH_SHORT).show()

                finish()

            }

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onStart() {
        super.onStart()
        mAuth!!.addAuthStateListener(mAuthListener!!)
    }

}
