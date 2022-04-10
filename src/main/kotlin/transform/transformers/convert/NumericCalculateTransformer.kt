package transform.transformers.convert

import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import transform.JsonData
import transform.Transformer

class NumericCalculateTransformer(val expression: (Int) -> (Int)) : Transformer() {
    override fun transform(jsonData: List<JsonData>): List<JsonData> {
        return jsonData.map {
            var visitedFieldName = mutableSetOf<Int>()
            val mainNode = mapper.readTree(it.getString())
            walk(mainNode) { currentNode, parentNode, fieldName, _ ->
                if (!visitedFieldName.contains(currentNode.hashCode())) {
                    if (currentNode.isInt && parentNode != null) {
                        if (parentNode.isObject) {
                            (parentNode as ObjectNode).put(
                                fieldName,
                                expression(currentNode.asInt())
                            )
                        } else if (parentNode.isArray) {
                            var array = parentNode as ArrayNode
                            var elements = array.elements().asSequence().toMutableList()
                            (0 until elements.size).forEach { i ->
                                var e = elements[i]
                                if (e.isInt) {
                                    array.set(i, expression(e.asInt()))
                                }
                            }
                        }
                    } else if (currentNode.isTextual && parentNode != null) {
                        try {
                            (parentNode as ObjectNode).put(
                                fieldName,
                                expression(currentNode.asText().toInt())
                            )
                        } catch (_: Exception) {
                        }
                    }
                }
                if (fieldName != null) {
                    visitedFieldName.add(currentNode.hashCode())
                }
            }
            JsonData(mapper.writeValueAsString(mainNode))
        }
    }
}
