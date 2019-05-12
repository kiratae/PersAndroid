package th.ac.buu.se.pers

import java.util.*


class QuestionData {

    var text: String? = null
    var status: String? = null
//    var time: Long = 0

    constructor(
        text: String,
        status: String
    ) {
        this.text = text
        this.status = status

//        this.time = Date().time
    }

    constructor() {}

}