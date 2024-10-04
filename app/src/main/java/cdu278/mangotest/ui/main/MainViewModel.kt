package cdu278.mangotest.ui.main

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cdu278.mangotest.auth.tokens.AuthUser
import cdu278.mangotest.auth.tokens.AuthUserStore
import cdu278.mangotest.ui.uiSharingStarted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @AuthUserStore
    private val authUserStore: DataStore<AuthUser?>,
) : ViewModel() {

    val authorizedFlow: Flow<Boolean> =
        authUserStore.data
            .map { it != null && it.exists }
            .stateIn(viewModelScope, uiSharingStarted, initialValue = true)
}