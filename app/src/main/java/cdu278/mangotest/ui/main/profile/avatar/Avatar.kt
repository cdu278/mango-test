package cdu278.mangotest.ui.main.profile.avatar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cdu278.mangotest.R
import cdu278.mangotest.ui.main.profile.avatar.AvatarUi.Local
import cdu278.mangotest.ui.main.profile.avatar.AvatarUi.Remote
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun Avatar(
    avatar: AvatarUi?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(percent = 50),
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
    ) {
        if (avatar != null) {
            GlideImage(
                model = when (avatar) {
                    is Local -> avatar.uri
                    is Remote -> avatar.url
                },
                contentScale = ContentScale.FillBounds,
                failure = placeholder(R.drawable.ic_broken_image),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
            )
        } else {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Icon(
                    painterResource(R.drawable.ic_image),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                )
            }
        }
    }
}