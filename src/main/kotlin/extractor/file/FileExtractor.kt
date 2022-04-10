package extractor.file

import extractor.Extractor
import transform.JsonData
import java.io.File

class FileExtractor(private val paths: List<String>) : Extractor {
    override fun extract(): List<JsonData> {
        val data = mutableListOf<JsonData>()
        paths.forEach {
            processFile(File(it), data)
        }
        return data
    }

    private fun processFile(file: File, data: MutableList<JsonData>) {
        if (file.exists()) {
            if (file.isFile && file.name.endsWith(".json")) {
                data.add(JsonData(file.readText()))
            } else if (file.isDirectory) {
                file.listFiles()?.forEach { childFile ->
                   processFile(childFile, data)
                }
            }
        }
    }
}
