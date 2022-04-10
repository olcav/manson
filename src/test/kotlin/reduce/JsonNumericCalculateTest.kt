package reduce

import Json
import noWs
import org.junit.jupiter.api.Test
import transform.TransformerFactory
import kotlin.test.assertEquals

class JsonNumericCalculateTest {

    @Test
    fun shouldSumNumericWithoutPredicate() {
        val jsonMerge = Json(
            """
            {
                "b" : "12",
                "a" : {
                    "b" : "c",
                    "abc" : "8",
                    "aa" :  {
                        "b" : [1,2,3,4] 
                        } 
                    }
           }
           """
        ).transform(
            TransformerFactory.numericCalculate {
                it * 2
            }
        )

        assertEquals(
            listOf(
                """
            {
                "b" : 24,
                "a" : {
                    "b" : "c",
                    "abc" : 16,
                    "aa" :  {
                        "b" : [2,4,6,8] 
                        } 
                    }
           }
                """.noWs()
            ),
            jsonMerge.getJsons()
        )
    }

    @Test
    fun shouldSumNumericWithPredicate() {
        val jsonMerge = Json(
            """
            {
                "b" : "18",
                "a" : {
                    "b" : "c",
                    "abc" : "8",
                    "aa" :  {
                        "b" : [1,2,3] 
                        } 
                    }
           }
           """
        ).transform(
            TransformerFactory.sumNumeric(predicateField = { it == "b" })
        )

        assertEquals(
            listOf("24"),
            jsonMerge.getJsons()
        )
    }
}