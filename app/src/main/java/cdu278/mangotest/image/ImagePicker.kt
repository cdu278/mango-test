package cdu278.mangotest.image

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class ImagePicker(
    private val activity: ComponentActivity,
) {

    private val _uriFlow = MutableSharedFlow<Uri>()
    val uriFlow: Flow<Uri>
        get() = _uriFlow

    private val resultLauncher =
        activity.registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                activity.lifecycleScope.launch { _uriFlow.emit(uri) }
            }
        }

    fun open() {
        resultLauncher.launch("image/*")
    }
}