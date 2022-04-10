package action.actions

import action.Action
import transform.JsonData
import java.io.File
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime

enum class WriteStrategy {
    REPLACE_IF_EXISTS,
    IGNORE_IF_EXISTS
}

enum class AppendValueType {
    INDEX,
    DATE,
    DATE_WITH_HOUR,
    TIMESTAMP
}

data class WriteFileOptions(
    val writeStrategy: WriteStrategy = WriteStrategy.REPLACE_IF_EXISTS,
    val appendValueType: AppendValueType = AppendValueType.INDEX,
    var defaultFileName: String = ""
)

class WriteFileAction(private val dir: String, private val options: WriteFileOptions? = WriteFileOptions()) : Action {
    override fun execute(data: List<JsonData>) {
        var i = 0
        data.forEach {
            val file = File("$dir/${generateName(i)}.json")
            val fileExists = file.exists()
            if (fileExists) {
                if (options?.writeStrategy == WriteStrategy.REPLACE_IF_EXISTS) {
                    file.delete()
                }
            }
            if (!(fileExists && options?.writeStrategy == WriteStrategy.IGNORE_IF_EXISTS)) {
                file.writeText(it.getPrettyString())
            }
            i++
        }
    }

    private fun generateName(index: Int): String {
        val name = options?.defaultFileName + "_"
        return when (options?.appendValueType) {
            AppendValueType.INDEX -> name + index
            AppendValueType.DATE -> name + LocalDate.now()
            AppendValueType.DATE_WITH_HOUR -> {
                name + LocalDateTime.now().withNano(0).toString()
                    .replaceFirst(':', 'h')
                    .replaceFirst(':', 'm') + "s"
            }
            AppendValueType.TIMESTAMP -> name + Instant.now().toEpochMilli()
            null -> index.toString()
        }
    }
}
