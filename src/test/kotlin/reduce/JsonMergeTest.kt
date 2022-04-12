package reduce

import Json
import noWs
import org.junit.jupiter.api.Test
import transform.TransformerFactory
import transform.transformers.reduce.ArrayMergeStrategy
import transform.transformers.reduce.MergeOption
import transform.transformers.reduce.NumericMergeStrategy
import kotlin.test.assertEquals

class JsonMergeTest {

    @Test
    fun shouldReturnFields() {
        val jsonMerge = Json(
            """
            {
                "b" : 12,
                "a" : {
                    "b" : "c", 
                    "aa" :  {
                        "b" : [1,2,12] 
                        } 
                    }
           }
           """,
            """
                    {
                    "b" : 13, 
                    "a" : { 
                          "d" : "aaa", 
                          "aa" :  {
                            "b" : [1,2,5,6,7,9]
                          }
                      }
                    }""",
            "{ \"test\": 1324 }"
        ).transform(
            TransformerFactory.merge(
                MergeOption(
                    arrayMergeStrategy = ArrayMergeStrategy.MERGE_AND_REMOVE_DUPLICATES
                )
            )
        )

        val expectMergeJson = """
                    {
                    "b": 13,
                    "a": {
                        "b":"c",
                        "aa": {
                            "b":[1,2,5,6,7,9,12]
                        },
                        "d":"aaa"
                    },
                    "test":1324
                    }
                    """.noWs()
        assertEquals(
            listOf(
                expectMergeJson
            ),
            jsonMerge.getJsons()
        )
    }

    @Test
    fun shouldSumNumericDuringMerge() {
        val jsonMerge = Json(
            """
            {
                "b" : 12,
                "d" : {
                    "e" : 15
                }
           }
           """,
            """
              {
                    "b" : 13,
                    "d" : {
                        "e" : 15
                     }
                }"""
        ).transform(
            TransformerFactory.merge(MergeOption(numericMergeStrategy = NumericMergeStrategy.SUM))
        )

        val expectMergeJson = """
                    {
                        "b" : 25,
                        "d" : { "e" : 30 }
                    }
                    """.noWs()
        assertEquals(
            expectMergeJson,
            jsonMerge.toString()
        )
    }
}
