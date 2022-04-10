import action.Action
import extractor.Extractor
import transform.JsonData
import transform.Transformer
import transform.transformers.reduce.ReduceTransformer

open class Json(vararg jsons: String) {

    protected var jsonsData: MutableList<JsonData> = jsons.map {
        JsonData(it)
    }.toMutableList()

    private constructor(jsonData: List<JsonData>) : this() {
        this.jsonsData = jsonData.toMutableList()
    }

    fun getJsons(): List<String> {
        return jsonsData.map {
            it.getString()
        }
    }

    /*
    fun getFieldValuesWithJsonPath(jsonPaths: List<String>): JsonPathFieldValues {
        return JsonPathFieldValues(jsons, jsons.flatMap { json ->
            val ctx = JsonPath.parse(json)
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
            }.toList()
        })
    }*/

    open fun getTotalSize(): Int {
        return jsonsData.sumOf { it.size() }
    }

    fun extract(vararg extractor: Extractor): Json {
        extractor.forEach {
            jsonsData.addAll(it.extract())
        }
        return this
    }

    fun transform(transformer: List<Transformer>): Json {
        return transform(*transformer.toTypedArray())
    }

    fun transform(vararg transforms: Transformer): Json {
        transforms.forEach {
            jsonsData = it.transform(jsonsData.toList()).toMutableList()
        }
        return this
    }

    /**
     * Apply group of transformers in parallel then joins all results.
     */
    fun parallelTransform(vararg transforms: List<Transformer>, joiner: ReduceTransformer? = null): Json {
        val jsonToTransforms = (transforms.indices).map {
            val jsonClone = Json(jsonsData.map { it.clone() })
            jsonClone.transform(transforms[it])
            jsonClone
        }
        val json = Json(jsonToTransforms.flatMap { it.jsonsData })
        if (joiner != null) {
            json.transform(joiner)
        }
        return json
    }

    fun parallelTransform(vararg transforms: Transformer, joiner: ReduceTransformer? = null): Json {
        return parallelTransform(
            *transforms.map { listOf(it) }.toTypedArray(), joiner = joiner
        )
    }

    fun execute(vararg actions: Action) {
        actions.forEach {
            it.execute(jsonsData)
        }
    }

    override fun toString(): String {
        return getJsons().joinToString()
    }
}
