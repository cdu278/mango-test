package cdu278.mangotest.ui.main

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cdu278.mangotest.auth.state.AuthState
import cdu278.mangotest.auth.state.AuthStateStore
import cdu278.mangotest.ui.uiSharingStarted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @AuthStateStore
    private val authStateStore: DataStore<AuthState>,
) : ViewModel() {

    val authorizedFlow: Flow<Boolean> =
        authStateStore.data
            .map { it is AuthState.Authorized }
            .stateIn(viewModelScope, uiSharingStarted, initialValue = true)
}