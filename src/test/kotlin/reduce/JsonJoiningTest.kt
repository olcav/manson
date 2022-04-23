package reduce

import Json
import flat
import org.junit.jupiter.api.Test
import transform.TransformerFactory
import kotlin.test.assertEquals

class JsonJoiningTest {

    // TODO : fixme
    //@Test
    fun shouldJoinJsons() {
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
            """.flat()
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
            """.flat(),
            jsonMerge.getJsons()[0].flat()
        )
    }
}
