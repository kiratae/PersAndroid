package th.ac.buu.se.myfirebasedemo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth? = FirebaseAuth.getInstance()
    var mAuthListener: FirebaseAuth.AuthStateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var btn_auth = findViewById<Button>(R.id.btn_auth)
        var btn_no_auth = findViewById<Button>(R.id.btn_no_auth)

        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                var intent = Intent(this.applicationContext, ChatActivity::class.java)
                startActivity(intent)
            } else {
                // User is signed out
            }
        }

        btn_auth.setOnClickListener {
            var intent = Intent(this.applicationContext, AuthActivity::class.java)
            startActivity(intent)
        }

        btn_no_auth.setOnClickListener {
            mAuth!!.signInAnonymously()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("FIREBASENAJA", "signInAnonymously:success")
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("FIREBASENAJA", "signInAnonymously:failure", task.exception)
                    }

                    // ...
                }

//            var intent = Intent(this.applicationContext, ChatActivity::class.java)
//            startActivity(intent)
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
