package extractor

import extractor.file.FileExtractor

object ExtractorFactory {
    fun fileExtract(paths: List<String>) = FileExtractor(paths)
}
