package cdu278.mangotest.datastore

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.first

suspend fun <T> DataStore<T>.value(): T = data.first()