package cdu278.mangotest.ui.main.profile

sealed interface ProfileErrorUi {

    data object EmptyName : ProfileErrorUi
}