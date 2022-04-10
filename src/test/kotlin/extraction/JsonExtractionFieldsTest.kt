package extraction

import Json
import org.junit.jupiter.api.Test
import transform.TransformerFactory
import kotlin.test.assertEquals

class JsonExtractionFieldsTest {

    @Test
    fun shouldReturnFields() {
        val jsonFields = Json("{\"b\" : 12, \"a\" : { \"b\" : \"c\", \"aa\" : [ {\"b\" : [1,2,3] } ]}}")
            .transform(
                TransformerFactory.fieldValuesExtraction("b")
            )
        assertEquals(
            listOf("12", "c", "1", "2", "3"),
            jsonFields.getJsons()
        )
    }
}
