package cdu278.mangotest.auth.tokens

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Qualifier

@OptIn(ExperimentalSerializationApi::class)
val Context.authUserDataStore: DataStore<AuthUser?>
    by dataStore(
        fileName = "auth-user.json",
        serializer = object : Serializer<AuthUser?> {

            override val defaultValue: AuthUser?
                get() = null

            override suspend fun readFrom(input: InputStream): AuthUser? {
                val bytes = withContext(Dispatchers.IO) {
                    input.use(InputStream::readBytes)
                }
                return bytes.takeIf { it.isNotEmpty() }
                    ?.let(::String)
                    ?.let(Json.Default::decodeFromString)
            }

            override suspend fun writeTo(t: AuthUser?, output: OutputStream) {
                withContext(Dispatchers.IO) {
                    output.use { stream ->
                        t?.let { Json.encodeToStream(it, stream) }
                    }
                }
            }
        },
    )

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthUserStore