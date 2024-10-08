package cdu278.mangotest.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import cdu278.mangotest.image.ImagePicker
import cdu278.mangotest.ui.root.Root
import cdu278.mangotest.ui.theme.MangoTestTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val imagePicker = ImagePicker(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MangoTestTheme {
                Root(
                    imagePicker,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
    }
}