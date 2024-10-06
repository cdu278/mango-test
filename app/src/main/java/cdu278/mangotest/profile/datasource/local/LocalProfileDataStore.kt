package cdu278.mangotest.profile.datasource.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import cdu278.mangotest.datastore.JsonSerializer

val Context.localProfileDataStore: DataStore<LocalProfile>
        by dataStore(
            fileName = "local-profile.json",
            serializer = JsonSerializer(
                LocalProfile.serializer(),
                defaultValue = LocalProfile.Absent,
            ),
        )