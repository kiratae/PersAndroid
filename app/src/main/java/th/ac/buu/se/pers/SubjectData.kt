package th.ac.buu.se.pers

import java.util.*


class SubjectData {

    var code: String? = null
    var name: String? = null
    var year: String? = null

    constructor(
        code: String,
        name: String,
        year: String
    ) {
        this.code = code
        this.name = name
        this.year = year
    }

    constructor() {}

}