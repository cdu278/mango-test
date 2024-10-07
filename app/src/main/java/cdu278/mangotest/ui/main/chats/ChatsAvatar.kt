package cdu278.mangotest.ui.main.chats

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import cdu278.mangotest.R
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ChatsAvatar(
    url: String,
    modifier: Modifier = Modifier
) {
    GlideImage(
        model = url,
        contentDescription = null,
        failure = placeholder(R.drawable.ic_broken_image),
        modifier = modifier
            .clip(CircleShape)
    )
}