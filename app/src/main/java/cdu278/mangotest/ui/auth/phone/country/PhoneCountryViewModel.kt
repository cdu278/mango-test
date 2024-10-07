package cdu278.mangotest.ui.auth.phone.country

import cdu278.mangotest.phone.PhoneCountry
import cdu278.mangotest.ui.uiSharingStarted
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PhoneCountryViewModel(
    private val coroutineScope: CoroutineScope,
    private val countries: List<PhoneCountry>,
    private val onCountryPicked: (PhoneCountry) -> Unit,
    val dismiss: () -> Unit,
) {

    private val _queryFlow = MutableStateFlow("")
    val queryFlow: StateFlow<String>
        get() = _queryFlow

    val countriesFlow: StateFlow<List<PhoneCountryUi>?> =
        channelFlow {
            var filtering: Job? = null
            queryFlow.collect { query ->
                filtering?.cancel()
                filtering = launch {
                    val filtered = withContext(Dispatchers.Default) {
                        countries.filter { it.name.contains(query, ignoreCase = true) }
                            .sortedBy { it.name }
                            .map {
                                PhoneCountryUi(
                                    code = it.code,
                                    name = it.name,
                                    phoneCode = "+${it.phoneCode}",
                                )
                            }
                    }
                    send(filtered)
                }
            }
        }.stateIn(coroutineScope, uiSharingStarted, initialValue = null)

    fun inputQuery(value: String) {
        _queryFlow.value = value
    }

    fun pickCountry(code: String) {
        coroutineScope.launch(Dispatchers.Default) {
            val country = countries.find { it.code == code }!!
            onCountryPicked(country)
            dismiss()
        }
    }
}