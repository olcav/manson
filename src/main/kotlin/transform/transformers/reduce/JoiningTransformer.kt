package transform.transformers.reduce

import transform.JsonData

class JoiningTransformer : ReduceTransformer() {
    override fun transform(jsonData: List<JsonData>): List<JsonData> {
        val array = mapper.createArrayNode()
        jsonData.forEach {
            array.add(it.getString())
        }
        return listOf(JsonData(mapper.writeValueAsString(array)))
    }
}
