package transform.transformers.extraction

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import transform.JsonData
import transform.Transformer

class FieldValuesExtractionTransformer(val field : String) : Transformer() {
    override fun transform(jsonData: List<JsonData>): List<JsonData> {
        fun extractFields(fields: Iterator<Map.Entry<String, JsonNode>>, addChild: Boolean = false): List<JsonNode> {
            return fields.asSequence().flatMap {
                val addAllChildren = it.key == field || addChild
                if (addChild || it.key == field) {
                    listOf(it.value)
                } else if (it.value.isObject) {
                    extractFields(it.value.fields(), addAllChildren)
                } else if (it.value.isArray) {
                    extractFields(
                        (it.value as ArrayNode).elements().asSequence().map {
                            Pair(it.toString(), it)
                        }.toMap().iterator(), addAllChildren
                    )
                } else {
                    emptyList()
                }
            }.toList()
        }

        fun fieldToText(node: JsonNode): List<String> {
            return if (node.isTextual) {
                listOf(node.textValue())
            } else if (node.isArray) {
                node.elements().asSequence().flatMap { fieldToText(it) }.toList()
            } else {
                listOf(node.toString())
            }
        }

        return jsonData.flatMap {
            val fields = mapper.readTree(it.getString()).fields()
            extractFields(fields).flatMap {
                fieldToText(it)
            }.map {
                JsonData(it)
            }
        }
    }
}
