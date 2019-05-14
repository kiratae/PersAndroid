package th.ac.buu.se.pers

import java.util.*


class QuestionData {

    var id: String? = null
    var text: String? = null
    constructor(
        id: String,
        text: String
    ) {
        this.id = id
        this.text = text
    }

    constructor() {}

    class ChoicesData{

        var text: String? = null
        var type: String? = null

        constructor(
            text: String,
            type: String
        ) {
            this.text = text
            this.type = type
        }

        constructor() {}
    }

}