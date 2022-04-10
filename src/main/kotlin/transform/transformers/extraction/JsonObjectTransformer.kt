package transform.transformers.extraction

import com.jayway.jsonpath.JsonPath
import transform.JsonData
import transform.Transformer

class JsonObjectTransformer(vararg val jsonPathMapping : Pair<String, String>) : Transformer() {
    override fun transform(jsonData: List<JsonData>): List<JsonData> {
        val objectNode = mapper.createObjectNode()
        jsonData.forEach { json ->
            val ctx = JsonPath.parse(json.getString())
            jsonPathMapping.forEach {
                val fieldName = it.first
                val jsonPath = it.second
                when (val values = ctx.read<Any>(jsonPath)) {
                    is List<*> -> {
                        val array = mapper.createArrayNode()
                        values.forEach {
                            if(it is Int){
                                array.add(it)
                            }else {
                                array.add(it.toString())
                            }
                        }
                        objectNode.set(fieldName, array)
                    }
                    is String -> {
                        objectNode.put(fieldName, values)
                    }
                    is Int -> {
                        objectNode.put(fieldName, values)
                    }
                    is LinkedHashMap<*, *> -> {
                        values.entries.forEach {
                            val value = it.value
                            if(value is String){
                                objectNode.put(it.key as String, value)
                            }else{
                                objectNode.replace(it.key as String, mapper.readTree(value.toString()))
                            }
                        }
                    }
                }
            }
        }
        return listOf(JsonData(objectNode))
    }
}
