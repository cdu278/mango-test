package cdu278.mangotest.ui.auth.phone.country

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cdu278.mangotest.R
import cdu278.mangotest.ui.SingleLineTextField
import cdu278.mangotest.ui.defaultMargin
import cdu278.mangotest.ui.halfMargin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneCountryPickerDialog(
    viewModel: PhoneCountryViewModel,
    modifier: Modifier = Modifier
) {
    BasicAlertDialog(
        onDismissRequest = viewModel.dismiss,
        modifier = modifier
    ) {
        Surface(
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surfaceContainer,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(defaultMargin)
            ) {
                Text(
                    stringResource(R.string.countryCodePicker_title),
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(Modifier.height(defaultMargin))

                val query by viewModel.queryFlow.collectAsState()
                SingleLineTextField(
                    value = query,
                    onValueChange = viewModel::inputQuery,
                    placeholder = { Text(stringResource(R.string.countryCodePicker_search)) },
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(Modifier.height(halfMargin))

                Box(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    val countriesState = viewModel.countriesFlow.collectAsState()
                    countriesState.value?.let { countries ->
                        if (countries.isNotEmpty()) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                items(countries, key = PhoneCountryUi::code) { country ->
                                    PhoneCountry(
                                        country,
                                        modifier = Modifier
                                            .clickable { viewModel.pickCountry(country.code) }
                                            .padding(defaultMargin)
                                    )
                                }
                            }
                        } else {
                            Text(
                                text = stringResource(R.string.countryCodePicker_nothingFound),
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .padding(top = 50.dp)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(halfMargin))
                TextButton(
                    onClick = viewModel.dismiss,
                    modifier = Modifier
                        .align(Alignment.End)
                ) {
                    Text(stringResource(R.string.countryCodePicker_close))
                }
            }
        }
    }
}