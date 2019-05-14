package th.ac.buu.se.pers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class QuestionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_show)

        //actionbar
        val actionbar = supportActionBar
        actionbar!!.title = "Question"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)

        val getIntent = intent
        val question_id: String? = getIntent.getStringExtra("question_id")

        Toast.makeText(this.applicationContext, question_id, Toast.LENGTH_LONG).show()
    }
}
