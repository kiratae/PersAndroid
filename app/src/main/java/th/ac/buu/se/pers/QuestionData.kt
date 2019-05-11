package th.ac.buu.se.pers

import java.util.*


class QuestionData {

    var id: String? = null
    var text: String? = null
    var status: String? = null
    var time: Long = 0

    constructor(id: String, text: String, status: String) {
        this.id = id
        this.text = text
        this.status = status

        this.time = Date().time
    }

    constructor() {}

}