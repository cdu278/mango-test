package cdu278.mangotest.ui

import kotlinx.coroutines.flow.SharingStarted

val uiSharingStarted: SharingStarted
    get() = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000)