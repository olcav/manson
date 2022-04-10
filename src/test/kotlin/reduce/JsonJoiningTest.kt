package reduce

import Json
import noWs
import org.junit.jupiter.api.Test
import transform.TransformerFactory
import kotlin.test.assertEquals

class JsonJoiningTest {
    @Test
    fun shouldReturnFields() {
        val jsonMerge = Json(
            """
            {
                "b" : 12,
                "a" : {
                    "b" : "c"
                },
                "d" : 10
           }
           """,
            """
            {
                "d" : 10,
                "e" : [1,2,3,4]
           }
            """.trimIndent().noWs()
        ).transform(
            TransformerFactory.joining()
        )

        assertEquals(
            """
            [{
                "b" : 12,
                "a" : {
                    "b" : "c"
                },
                "d" : 10
           },{
                "d" : 10,
                "e" : [1,2,3,4]
           }]
            """.trimIndent().noWs(),
            jsonMerge.getJsons()[0].trimIndent().noWs()
        )
    }
}
