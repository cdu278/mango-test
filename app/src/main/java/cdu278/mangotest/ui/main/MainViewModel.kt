package cdu278.mangotest.ui.main

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cdu278.mangotest.auth.token.AuthTokens
import cdu278.mangotest.auth.token.AuthTokensStore
import cdu278.mangotest.ui.uiSharingStarted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @AuthTokensStore
    private val tokensStore: DataStore<AuthTokens?>,
) : ViewModel() {

    val authorizedFlow: Flow<Boolean> =
        tokensStore.data
            .map { it != null }
            .stateIn(viewModelScope, uiSharingStarted, initialValue = true)
}