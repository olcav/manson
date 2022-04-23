package remove

import Json
import flat
import org.junit.jupiter.api.Test
import transform.TransformerFactory
import kotlin.test.assertEquals

class RemoveNullsTransformerTest {
    @Test
    fun shouldRemoveNullsValues() {
        val json = Json(
            """
           {
            "b" : null, 
            "a" : {
                "b" : "c",
                "aa" : [ null, 3 , null],
                "d" : null
                },
            "d" : ["dd", null]
           }
        """
        )
        val jsonObject = json.transform(
            TransformerFactory.removeNulls()
        )
        assertEquals(
            listOf(
                """
           {
            "a" : {
                "b" : "c",
                "aa" : [ 3 ]
                },
            "d" : ["dd"]
           }
        """.flat()
            ),
            jsonObject.getJsons()
        )
    }
}
