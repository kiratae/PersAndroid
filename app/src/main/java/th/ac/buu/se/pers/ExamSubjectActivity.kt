package th.ac.buu.se.pers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class ExamSubjectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exam_subject)

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Exam Subject"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
