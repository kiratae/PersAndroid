package th.ac.buu.se.pers

import java.util.*


class SubjectData {

    var id: String? = null
    var code: String? = null
    var name: String? = null
    var year: String? = null

    constructor(
        id: String,
        code: String,
        name: String,
        year: String
    ) {
        this.id = id
        this.code = code
        this.name = name
        this.year = year
    }

    constructor() {}

}