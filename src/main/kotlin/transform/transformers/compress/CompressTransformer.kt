package transform.transformers.compress

import transform.JsonData
import transform.Transformer
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets
import java.util.zip.GZIPOutputStream

class CompressTransformer : Transformer() {
    override fun transform(jsonData: List<JsonData>): List<JsonData> {
        return jsonData.map { content ->
            val bos = ByteArrayOutputStream()
            GZIPOutputStream(bos).bufferedWriter(StandardCharsets.UTF_8).use { it.write(content.getString()) }
            JsonData(bos.toByteArray())
        }
    }
}
