package cdu278.mangotest.ui.main.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cdu278.mangotest.R
import cdu278.mangotest.image.ImagePicker
import cdu278.mangotest.ui.SingleLineTextField
import cdu278.mangotest.ui.defaultMargin
import cdu278.mangotest.ui.error.request.RequestErrorDialog
import cdu278.mangotest.ui.format
import cdu278.mangotest.ui.halfMargin
import cdu278.mangotest.ui.main.profile.avatar.Avatar
import cdu278.mangotest.ui.main.profile.dialog.ProfileDialog
import cdu278.mangotest.zodiac.ZodiacSign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    imagePicker: ImagePicker,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(imagePicker) {
        imagePicker.uriFlow.collect {
            viewModel.inputAvatar(uri = it)
        }
    }
    Column(
        modifier = modifier
    ) {
        val errorDialog by viewModel.errorDialogFlow.collectAsState()
        RequestErrorDialog(
            errorDialog,
            onDismiss = viewModel::dismissDialog,
        )

        val dialog by viewModel.dialogFlow.collectAsState()
        ProfileDialog(
            dialog,
            onBirthdaySelected = viewModel::inputBirthday,
            logOut = viewModel::confirmLogOut,
            onDismiss = viewModel::dismissDialog,
        )

        val modelState = viewModel.modelFlow.collectAsState()
        val model = modelState.value ?: return

        val canSave = model.data?.canSave == true

        TopAppBar(
            title = { Text(stringResource(R.string.main_profile)) },
            actions = {
                if (canSave) {
                    IconButton(
                        onClick = viewModel::save,
                        enabled = !model.loading,
                    ) {
                        Icon(painterResource(R.drawable.ic_save), contentDescription = null)
                    }
                }
                IconButton(onClick = viewModel::logOut) {
                    Icon(painterResource(R.drawable.ic_logout), contentDescription = null)
                }
            },
        )

        PullToRefreshBox(
            isRefreshing = model.loading,
            onRefresh = viewModel::refresh,
            modifier = Modifier
                .weight(1f)
        ) {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(halfMargin),
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if (model.syncFailure != null) {
                    item("syncFailure") {
                        SyncFailureCard(model.syncFailure)
                        Spacer(Modifier.height(defaultMargin))
                    }
                }

                val data = model.data ?: return@LazyColumn

                item("avatar") {
                    Avatar(
                        data.avatar,
                        onClick = imagePicker::open,
                        modifier = Modifier
                            .size(150.dp)
                    )
                    Spacer(Modifier.height(defaultMargin))
                }

                item("username") {
                    SingleLineTextField(
                        value = data.username,
                        onValueChange = { },
                        label = { Text(stringResource(R.string.profile_username)) },
                        readOnly = true,
                    )
                }
                item("phone") {
                    SingleLineTextField(
                        value = data.phone,
                        onValueChange = { },
                        label = { Text(stringResource(R.string.profile_phone)) },
                        readOnly = true,
                    )
                    Spacer(Modifier.height(defaultMargin))
                }

                item("name") {
                    SingleLineTextField(
                        value = data.name,
                        onValueChange = viewModel::inputName,
                        label = { Text(stringResource(R.string.profile_name)) },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                            imeAction = ImeAction.Next,
                        ),
                        enabled = !model.loading,
                        isError = data.error is ProfileErrorUi.EmptyName,
                    )
                    Spacer(Modifier.height(defaultMargin))
                }

                item("city") {
                    SingleLineTextField(
                        value = data.city,
                        onValueChange = viewModel::inputCity,
                        label = { Text(stringResource(R.string.profile_city)) },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            imeAction = ImeAction.Next,
                        ),
                        enabled = !model.loading,
                    )
                    Spacer(Modifier.height(defaultMargin))
                }

                item("birthday") {
                    SingleLineTextField(
                        value = data.birthday?.format() ?: "",
                        onValueChange = { },
                        label = { Text(stringResource(R.string.profile_birthday)) },
                        readOnly = true,
                        trailingIcon = if (!model.loading) {
                            {
                                IconButton(onClick = viewModel::pickBirthday) {
                                    Icon(
                                        painterResource(R.drawable.ic_edit),
                                        contentDescription = null
                                    )
                                }
                            }
                        } else {
                            null
                        },
                    )
                }
                item("zodiac") {
                    data.zodiacSign?.let { zodiacSign ->
                        SingleLineTextField(
                            value = zodiacSign.text,
                            onValueChange = { },
                            label = { Text(stringResource(R.string.profile_zodiac)) },
                            readOnly = true,
                        )
                    }
                    Spacer(Modifier.height(defaultMargin))
                }

                item("status") {
                    OutlinedTextField(
                        value = data.status,
                        onValueChange = viewModel::inputStatus,
                        label = { Text(stringResource(R.string.profile_status)) },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            imeAction = ImeAction.None
                        ),
                        modifier = Modifier
                            .heightIn(max = 150.dp)
                    )
                }
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(halfMargin)
        ) {
            Text(
                text = when (model.data?.error) {
                    ProfileErrorUi.EmptyName -> stringResource(R.string.profile_error_emptyName)
                    null -> ""
                },
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.error,
            )
            Button(
                onClick = viewModel::save,
                enabled = canSave && !model.loading,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(stringResource(R.string.profile_save))
            }
        }
    }
}

private val ZodiacSign.text: String
    @Composable
    get() = stringArrayResource(R.array.zodiacSigns)[this.ordinal]