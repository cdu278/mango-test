package cdu278.mangotest.ui.main.profile

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cdu278.mangotest.R
import cdu278.mangotest.ui.halfMargin
import cdu278.mangotest.ui.main.profile.ProfileUi.SyncFailure.ConnectionError
import cdu278.mangotest.ui.main.profile.ProfileUi.SyncFailure.UnknownError

@Composable
fun SyncFailureCard(
    failure: ProfileUi.SyncFailure,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
        ),
        modifier = modifier
    ) {
        Text(
            text = stringResource(
                id = when (failure) {
                    ConnectionError -> R.string.profile_syncFailure_connection
                    UnknownError -> R.string.profile_syncFailure_unknown
                }
            ),
            modifier = Modifier
                .padding(halfMargin)
        )
    }
}