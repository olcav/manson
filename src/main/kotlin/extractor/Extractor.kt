package extractor

import transform.JsonData

interface Extractor {
    fun extract(): List<JsonData>
}
