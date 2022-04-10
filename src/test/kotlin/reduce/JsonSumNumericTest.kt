package reduce

import Json
import org.junit.jupiter.api.Test
import transform.TransformerFactory
import kotlin.test.assertEquals

class JsonSumNumericTest {

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
                        "b" : [1,2,3] 
                        } 
                    }
           }
           """
        ).transform(
            TransformerFactory.sumNumeric()
        )

        assertEquals(
            listOf("26"),
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
