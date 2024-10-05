package cdu278.mangotest.auth.state

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import cdu278.mangotest.datastore.JsonSerializer

val Context.authStateDataStore: DataStore<AuthState>
    by dataStore(
        fileName = "auth-state.json",
        serializer = JsonSerializer(
            AuthState.serializer(),
            defaultValue = AuthState.NotAuthorized,
        ),
    )