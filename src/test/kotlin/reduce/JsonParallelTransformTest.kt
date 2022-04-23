package reduce

import Json
import org.junit.jupiter.api.Test
import transform.TransformerFactory
import kotlin.test.assertEquals

class JsonParallelTransformTest {

    @Test
    fun shouldPerformParallelTransformationAndSumNumeric() {
        val jsonMerge = Json(
            """
            {
                "b" : 12,
                "a" : {
                    "b" : "c", 
                    "aa" :  {
                        "b" : [1,2,3] 
                        } 
                    },
                "d" : 10
           }
           """
        )
            // Execute each transformers in parallel then join the result with another transformer
            .parallelTransform(
                TransformerFactory.fieldValuesExtraction("aa"),
                TransformerFactory.fieldValuesExtraction("d"),
                joiner = TransformerFactory.sumNumeric()
            )

        assertEquals(
            listOf("16"), jsonMerge.getJsons()
        )
    }
}
