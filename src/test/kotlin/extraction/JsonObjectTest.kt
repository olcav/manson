package extraction

import Json
import flat
import org.junit.jupiter.api.Test
import transform.TransformerFactory
import kotlin.test.assertEquals

class JsonObjectTest {
    @Test
    fun shouldConstructJsonObject() {
        val json = Json( """
                {
                "b" : 12,
                "a" : { 
                    "b" : "c", 
                    "aa" : [ {"b" : [1,2,3] } ]
                }, 
                "d" : ["dd"]
            }
        """)
        val jsonObject = json.transform(
            TransformerFactory.jsonObject(
                "numeric" to "$.a.aa[0].b[2]",
                "object" to "$.a",
                "array" to "$.a.aa[0].b",
                "char" to "$.a.b",
                "arrayChar" to "$.d"
            )
        )
        assertEquals(
            """
                {
                    "numeric":3, 
                    "b":"c",
                    "aa": [ { "b": [1,2,3] }],
                    "array": [1,2,3],
                    "char":"c",
                    "arrayChar": ["dd"]
                }""".flat(),
            jsonObject.getJsons()[0].flat()
        )
    }
}
