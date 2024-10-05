package cdu278.mangotest.datastore

import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.io.InputStream
import java.io.OutputStream

@OptIn(ExperimentalSerializationApi::class)
class JsonSerializer<T>(
    private val kSerializer: KSerializer<T>,
    override val defaultValue: T,
    private val json: Json = Json,
) : Serializer<T> {

    override suspend fun readFrom(input: InputStream): T {
        return withContext(Dispatchers.IO) {
            input.use { json.decodeFromStream(kSerializer, stream = it) }
        }
    }

    override suspend fun writeTo(t: T, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.use { json.encodeToStream(kSerializer, t, stream = it) }
        }
    }
}