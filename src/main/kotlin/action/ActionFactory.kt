package action

import action.actions.ForEachAction
import action.actions.WriteFileAction
import action.actions.WriteFileOptions
import transform.JsonData

object ActionFactory {
    fun forEach(action: (JsonData, Int) -> Unit) = ForEachAction(action)
    fun writeFile(dir: String, options: WriteFileOptions?) = WriteFileAction(dir, options)
}
