package transform

import transform.transformers.compress.CompressTransformer
import transform.transformers.compress.UncompressTransformer
import transform.transformers.convert.NumericCalculateTransformer
import transform.transformers.extraction.FieldValuesExtractionTransformer
import transform.transformers.extraction.JsonObjectTransformer
import transform.transformers.reduce.JoiningTransformer
import transform.transformers.reduce.MergeOption
import transform.transformers.reduce.MergeTransformer
import transform.transformers.reduce.SumNumericTransformer
import transform.transformers.remove.RemoveNullsOption
import transform.transformers.remove.RemoveNullsTransformer

object TransformerFactory {
    fun compress() = CompressTransformer()
    fun uncompress() = UncompressTransformer()

    fun merge(options: MergeOption = MergeOption()) = MergeTransformer(options)

    fun fieldValuesExtraction(field: String) = FieldValuesExtractionTransformer(field)

    fun sumNumeric(predicateField: (String) -> Boolean = { true }) = SumNumericTransformer(predicateField)
    fun numericCalculate(expression: (Int) -> (Int) = { x -> x }) = NumericCalculateTransformer(expression)
    fun jsonObject(vararg jsonPathMapping: Pair<String, String>) = JsonObjectTransformer(*jsonPathMapping)

    fun removeNulls(options: RemoveNullsOption = RemoveNullsOption()) = RemoveNullsTransformer(options)

    fun joining() = JoiningTransformer()
}
