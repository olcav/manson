package extractor

import Json
import action.ActionFactory
import org.junit.jupiter.api.Test
import transform.TransformerFactory
import transform.transformers.reduce.ArrayMergeStrategy
import transform.transformers.reduce.MergeOption

class ExtractorTest {
    @Test
    fun shouldExtract() {
        Json()
            .extract(
                ExtractorFactory.fileExtract(listOf("./src/test/resources"))
            )
            .transform(TransformerFactory.merge(
                MergeOption(
                    arrayMergeStrategy = ArrayMergeStrategy.MERGE_AND_REMOVE_DUPLICATES
                )
            ))
            .execute(
                ActionFactory.forEach { jsonData, i ->
                    println(jsonData.getPrettyString())
                }
            )

    }
}
