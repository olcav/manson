package transform

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

abstract class Transformer {
    protected val mapper = jacksonObjectMapper()
    abstract fun transform(jsonData: List<JsonData>): List<JsonData>

    fun walk(
        curNode: JsonNode,
        parentNode: JsonNode? = null,
        predicateField: (String) -> Boolean = { true },
        curFieldName: String? = null,
        jsonNodeMapping: (JsonNode, JsonNode?, String?, MutableIterator<String>?) -> Unit
    ) {
        if (curNode.isEmpty) {
            jsonNodeMapping(curNode, parentNode, curFieldName, null)
        }
        val fieldNames = curNode.fieldNames()
        while (fieldNames.hasNext()) {
            val fieldName = fieldNames.next()
            val jsonNode = curNode.get(fieldName)
            if (jsonNode.isObject) {
                walk(jsonNode, curNode, predicateField, fieldName, jsonNodeMapping)
            } else if (jsonNode.isArray) {
                jsonNode.elements().forEach {
                    if (it.isObject || it.isArray) {
                        walk(it, jsonNode, predicateField, fieldName, jsonNodeMapping)
                    } else if (predicateField(fieldName)) {
                        jsonNodeMapping(it, jsonNode, fieldName, fieldNames)
                    }
                }
            } else if (predicateField(fieldName)) {
                jsonNodeMapping(jsonNode, curNode, fieldName, fieldNames)
            }
        }
    }
}
