package transform.transformers.remove

import com.fasterxml.jackson.databind.node.ArrayNode
import transform.JsonData
import transform.Transformer

enum class RemoveNullsArrayStrategy {
    ALWAYS_REMOVE_NULLS,
    NO_REMOVE_NULLS
}

data class RemoveNullsOption(
    val removeNullsArrayStrategy: RemoveNullsArrayStrategy = RemoveNullsArrayStrategy.ALWAYS_REMOVE_NULLS
)

class RemoveNullsTransformer(val options: RemoveNullsOption) : Transformer() {
    override fun transform(jsonData: List<JsonData>): List<JsonData> {
        return jsonData
            .map {
                val mainNode = mapper.readTree(it.getString())
                walk(mainNode) { curNode, node, _, fieldNames ->
                    if (curNode.isNull && node != null && !node.isArray) {
                        fieldNames?.remove()
                    } else if (node != null && node.isArray &&
                        options.removeNullsArrayStrategy == RemoveNullsArrayStrategy.ALWAYS_REMOVE_NULLS) {
                        val a = node as ArrayNode
                        a.removeAll {
                            it.isNull
                        }
                    }
                }
                JsonData(mainNode.toString())
            }
    }
}
