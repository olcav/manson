package transform.transformers.reduce

import com.fasterxml.jackson.databind.node.ArrayNode

enum class ArrayMergeStrategy(val mergeFunction: (ArrayNode, ArrayNode) -> ArrayNode) {
    REPLACE_ARRAY({ x, y -> x }),
    MERGE_ARRAY({ x, y -> x.addAll(y) }),
    MERGE_AND_REMOVE_DUPLICATES({ x, y ->
        val newArray = com.fasterxml.jackson.module.kotlin.jacksonObjectMapper().createArrayNode()
        val nodes = mutableSetOf<com.fasterxml.jackson.databind.JsonNode>()
        nodes.addAll(x.toMutableList())
        nodes.addAll(y.toMutableList())
        newArray.addAll(nodes)
    })
}

enum class NumericMergeStrategy(val mergeFunction: (Int, Int) -> Int) {
    REPLACE({ x, _ -> x }),
    SUM({ x, y -> x + y }),
    AVG({ x, y -> (x + y) / 2 }),
    MIN({ x, y -> x.coerceAtMost(y) }),
    MAX({ x, y -> x.coerceAtLeast(y) }),
    RANDOM({ x, y ->
        if ((0..1).random() == 0) {
            x
        } else {
            y
        }
    })
}

data class MergeOption(
    val numericMergeStrategy: NumericMergeStrategy = NumericMergeStrategy.REPLACE,
    val arrayMergeStrategy: ArrayMergeStrategy = ArrayMergeStrategy.REPLACE_ARRAY
)
