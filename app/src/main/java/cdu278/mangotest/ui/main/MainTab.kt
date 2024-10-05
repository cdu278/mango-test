package cdu278.mangotest.ui.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import cdu278.mangotest.R

data class MainTab(
    @StringRes val title: Int,
    val route: Any,
    @DrawableRes val icon: Int,
)

val MainTabs: List<MainTab> =
    listOf(
        MainTab(
            title = R.string.main_tab_chats,
            route = MainDestinations.Chats,
            icon = R.drawable.ic_chat,
        ),
        MainTab(
            title = R.string.main_tab_profile,
            route = MainDestinations.Profile,
            icon = R.drawable.ic_profile,
        ),
    )