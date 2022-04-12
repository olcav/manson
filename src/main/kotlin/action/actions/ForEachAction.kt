package action.actions

import action.Action
import transform.JsonData

/**
 * Execute a function on each json data.
 */
class ForEachAction(val action: (JsonData, Int) -> Unit) : Action {
    override fun execute(data: List<JsonData>) {
        var i = 0
        data.forEach {
            action(it, i)
            i++
        }
    }
}
