import action.Action
import extractor.Extractor
import transform.JsonData
import transform.Transformer
import transform.transformers.reduce.ReduceTransformer

open class Json(vararg jsons: String) {

    private var jsonsData: MutableList<JsonData> = jsons.map {
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
    private fun parallelTransform(vararg transforms: List<Transformer>, joiner: ReduceTransformer? = null): Json {
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
