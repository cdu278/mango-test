package cdu278.mangotest.auth

sealed interface PhoneNumber {

    data object NotValid : PhoneNumber

    class Valid(val value: String) : PhoneNumber
}