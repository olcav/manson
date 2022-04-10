package compress

import Json
import org.junit.jupiter.api.Test
import transform.TransformerFactory
import kotlin.test.assertEquals

class JsonCompressTest {

    @Test
    fun shouldCompressJson() {
        val originalJson = Json(
            """
                    {
                    "b" : 13, 
                    "a" : { 
                          "d" : "aaa", 
                          "aa" :  {
                            "b" : [5,6,7]
                          }
                      }
                    }"""
        )
        val compress = originalJson.transform(
            TransformerFactory.compress(),
            TransformerFactory.uncompress()
        )
        assertEquals(originalJson, compress)
    }
}