package transform.transformers.reduce

import transform.JsonData

class SumNumericTransformer(private val predicateField: (String) -> Boolean = { true }) : ReduceTransformer() {

    override fun transform(jsonData: List<JsonData>): List<JsonData> {
        return listOf(
            JsonData(
                jsonData
                    .flatMap {
                        val ints = mutableListOf<Int>()
                        if (it.isInt()) {
                            val element = it.getInt()
                            if (element != null) {
                                ints.add(element)
                            }
                        } else {
                            try {
                                val mainNode = mapper.readTree(it.getString())
                                walk(mainNode, predicateField = predicateField) { curNode, _, _, _ ->
                                    if (curNode.isInt) {
                                        ints.add(curNode.asInt())
                                    } else if (curNode.isTextual) {
                                        try {
                                            ints.add(curNode.asText().toInt())
                                        } catch (_: Exception) {
                                        }
                                    }
                                }
                            } catch (_: Exception) {
                            }
                        }
                        ints
                    }.sum()
            )
        )
    }
}
