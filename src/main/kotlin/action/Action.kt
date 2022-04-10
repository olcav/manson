package action

import transform.JsonData

interface Action {
    fun execute(data: List<JsonData>)
}
