package th.ac.buu.se.pers

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    var main_menu: Menu? = null
    var isLogin = false
    var actionbar: ActionBar? = null

    var mAuth: FirebaseAuth? = FirebaseAuth.getInstance()
    var mAuthListener: FirebaseAuth.AuthStateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        actionbar!!.title = "Pers"

        var questionBtn = findViewById<Button>(R.id.question_btn)
        var examBtn = findViewById<Button>(R.id.examination_btn)
        examBtn.isEnabled = false

        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if(user != null){
                isLogin = true
            }else{
                isLogin = false
                finish()
            }
        }

        questionBtn.setOnClickListener {
            Toast.makeText(this.applicationContext, "Question Click", Toast.LENGTH_SHORT).show()
            var intent = Intent(this.applicationContext, QuestionSubjectActivity::class.java)
            startActivity(intent)
        }

        examBtn.setOnClickListener {
            Toast.makeText(this.applicationContext, "Exam Click", Toast.LENGTH_SHORT).show()
            var intent = Intent(this.applicationContext, ExamSubjectActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu, menu)
        main_menu = menu

        // check firebase login if login show power icon else show face icon
        if (!isLogin) {
            main_menu!!.getItem(0).icon = ContextCompat.getDrawable(this, R.drawable.ic_face_black_24dp)
        } else {
            main_menu!!.getItem(0).icon = ContextCompat.getDrawable(this, R.drawable.ic_power_settings_new_black_24dp)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // Handle item selection
        return when (item!!.itemId) {
            R.id.action_login_logout -> {
                mAuth!!.signOut()
                Toast.makeText(this, "Sign Out", Toast.LENGTH_SHORT).show()

                true
            }
            else -> super.onOptionsItemSelected(item)
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

}
