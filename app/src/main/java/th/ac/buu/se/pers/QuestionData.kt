package th.ac.buu.se.pers

import java.util.*


class QuestionData {

    var text: String? = null
    var status: String? = null

    constructor(
        text: String,
        status: String
    ) {
        this.text = text
        this.status = status
    }

    constructor() {}

}