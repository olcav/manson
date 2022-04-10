package transform.transformers.compress

import transform.JsonData
import transform.Transformer
import java.nio.charset.StandardCharsets
import java.util.zip.GZIPInputStream

class UncompressTransformer : Transformer() {
    override fun transform(jsonData: List<JsonData>): List<JsonData> {
        return jsonData.map { content ->
            JsonData(
                GZIPInputStream(content.getInputStream()).bufferedReader(StandardCharsets.UTF_8).use { it.readText() }
            )
        }
    }
}
