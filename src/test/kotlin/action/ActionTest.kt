package action

import Json
import org.junit.jupiter.api.Test
import transform.TransformerFactory
import kotlin.test.assertEquals

class ActionTest {

    @Test
    fun shouldTestForEachAction() {
        Json(
            """
                    {
                    "b" : 13, 
                    "a" : { 
                          "d" : "aaa", 
                          "aa" :  {
                            "b" : [5,6,7]
                          }
                      }
                    }""", """
                    {
                    "a" : { 
                          "d" : "1", 
                          "aa" :  {
                            "b" : [8,6]
                          }
                      }
                    }"""
        ).transform(
            TransformerFactory.fieldValuesExtraction("a")
        ).execute(
            ActionFactory.forEach { jsonData, i ->
                if (i == 0) {
                    assertEquals("{\"d\":\"aaa\",\"aa\":{\"b\":[5,6,7]}}", jsonData.getString())
                } else if (i == 1) {
                    assertEquals("{\"d\":\"1\",\"aa\":{\"b\":[8,6]}}", jsonData.getString())
                }
            })
    }
}
