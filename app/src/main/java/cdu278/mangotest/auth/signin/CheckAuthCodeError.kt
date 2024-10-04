package cdu278.mangotest.auth.signin

import cdu278.mangotest.http.ValidatedRequestError

sealed interface CheckAuthCodeError {

    data object InvalidCode : CheckAuthCodeError

    class Wrapped(val error: ValidatedRequestError) : CheckAuthCodeError
}