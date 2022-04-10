package transform

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.InputStream

class JsonData(private val jsonData: Any) {
    fun getString(): String {
        // TODO : handle bytearray
        return jsonData.toString()
    }

    fun getPrettyString(): String {
        val mapper = jacksonObjectMapper()
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
            mapper.readValue(getString(), Any::class.java)
        )
    }

    fun getInputStream(): InputStream {
        return when (jsonData) {
            is ByteArray -> jsonData.inputStream()
            is String -> jsonData.byteInputStream()
            else -> jsonData.toString().byteInputStream()
        }
    }

    fun getInt(): Int? {
        if (jsonData is Int) {
            return jsonData
        }
        return try {
            jsonData.toString().toInt()
        } catch (e: Exception) {
            null
        }
    }

    fun isInt(): Boolean {
        if (jsonData is Int) {
            return true
        }
        return try {
            jsonData.toString().toInt() is Int
        } catch (e: Exception) {
            false
        }
    }

    fun size(): Int {
        return when (jsonData) {
            is ByteArray -> jsonData.size
            is String -> jsonData.encodeToByteArray().size
            else -> jsonData.toString().length
        }
    }

    fun clone(): JsonData {
        return JsonData(jsonData)
    }
}
