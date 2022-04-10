package transform.transformers.extraction

import com.jayway.jsonpath.JsonPath
import transform.JsonData
import transform.Transformer

class JsonPathValuesExtractionTransformer(vararg val jsonPaths: String) : Transformer() {
    override fun transform(jsonData: List<JsonData>): List<JsonData> {
        return jsonData.flatMap {
            val ctx = JsonPath.parse(it.getString())
            jsonPaths.flatMap {
                when (val values = ctx.read<Any>(it)) {
                    is List<*> -> {
                        values.map { it.toString() }
                    }
                    is String -> {
                        listOf(values)
                    }
                    else -> {
                        listOf()
                    }
                }
            }.map {
                JsonData(it)
            }.toList()
        }
    }
}
