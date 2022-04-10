package action

import Json
import action.actions.AppendValueType
import action.actions.WriteFileOptions
import action.actions.WriteStrategy
import org.junit.jupiter.api.Test
import transform.TransformerFactory
import kotlin.test.assertEquals

class WriteFileActionTest {

    @Test
    fun should() {
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
                          "aa" :  { "b" : [8,6] }
                      }
                    }"""
        ).execute(
            ActionFactory.writeFile(".", WriteFileOptions(
                defaultFileName = "json_files",
                appendValueType = AppendValueType.TIMESTAMP
            ))
        )
    }
}