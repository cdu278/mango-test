package cdu278.mangotest.image

import android.content.Context
import android.net.Uri
import android.util.Base64
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ImageBase64Converter @Inject constructor(
    @ApplicationContext
    private val context: Context,
) {

    suspend fun convert(imageUri: Uri): String {
        return withContext(Dispatchers.Default) {
            val bytes = withContext(Dispatchers.IO) {
                context.contentResolver
                    .openInputStream(imageUri)!!
                    .use { it.readBytes() }
            }
            String(Base64.encode(bytes, Base64.DEFAULT))
        }
    }
}