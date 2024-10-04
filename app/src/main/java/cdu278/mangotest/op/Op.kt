package cdu278.mangotest.op

import cdu278.mangotest.op.results.OpResults

interface Op<out T, out Error> {

    interface Results<in T, in Error, out R> {

        suspend fun success(value: T): R

        suspend fun failure(error: Error): R
    }

    suspend fun <R> perform(results: Results<T, Error, R>): R

    class Success<T>(
        private val value: T
    ) : Op<T, Nothing> {

        override suspend fun <R> perform(results: Results<T, Nothing, R>): R {
            return results.success(value)
        }
    }

    class Failure<Error>(
        private val error: Error
    ) : Op<Nothing, Error> {

        override suspend fun <R> perform(results: Results<Nothing, Error, R>): R {
            return results.failure(error)
        }
    }
}

suspend fun <T, Error, R> Op<T, Error>.fold(
    ifSuccess: suspend (T) -> R,
    ifFailure: suspend (Error) -> R
): R {
    return this.perform(OpResults(ifSuccess, ifFailure))
}

suspend fun <T> Op<T, *>.resultOrNull(): T? {
    return this.fold(ifSuccess = { it }, ifFailure = { null })
}