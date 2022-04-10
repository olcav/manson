package transform.transformers.reduce

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import transform.JsonData

class MergeTransformer(val options: MergeOption) : ReduceTransformer() {

    private fun mergeNode(mainNode: JsonNode, updateNode: JsonNode): JsonNode {
        val fieldNames = updateNode.fieldNames()
        while (fieldNames.hasNext()) {
            val fieldName = fieldNames.next()
            val jsonNode = mainNode.get(fieldName)
            if (jsonNode != null && jsonNode.isObject) {
                mergeNode(jsonNode, updateNode.get(fieldName))
            } else {
                if (mainNode is ObjectNode) {
                    val valueToSet = updateNode.get(fieldName)
                    if (valueToSet is ArrayNode && jsonNode is ArrayNode) {
                        mainNode.replace(fieldName, options.arrayMergeStrategy.mergeFunction(valueToSet, jsonNode))
                    } else {
                        if (valueToSet.isNumber && jsonNode != null && jsonNode.isNumber) {
                            mainNode.put(
                                fieldName,
                                options.numericMergeStrategy.mergeFunction(valueToSet.asInt(), jsonNode.asInt())
                            )
                        } else {
                            mainNode.replace(fieldName, valueToSet)
                        }
                    }
                }
            }
        }
        return mainNode
    }

    override fun transform(jsonData: List<JsonData>): List<JsonData> {
        return if (jsonData.isEmpty()) {
            emptyList()
        } else if (jsonData.size == 1) {
            jsonData
        } else {
            var mergedNode: JsonNode = mapper.readTree(jsonData.first().getString())
            (1 until jsonData.size).forEach {
                val jsonNode = mapper.readTree(jsonData[it].getString())
                mergedNode = mergeNode(mergedNode, jsonNode)
            }
            listOf(JsonData(mergedNode))
        }
    }

}
