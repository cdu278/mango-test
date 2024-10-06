package cdu278.mangotest.op.error

import cdu278.mangotest.op.Op

class CollectingErrorsResults<T, Error>(
    private val ifSuccess: suspend (T) -> Unit,
    private val errorCollection: OpErrorCollection<Error>,
) : Op.Results<T, Error, Unit> {

    override suspend fun success(value: T) {
        ifSuccess(value)
    }

    override suspend fun failure(error: Error) {
        errorCollection.add(error)
    }
}

fun <T, Error> collectingErrors(
    errorCollection: OpErrorCollection<Error>,
    ifSuccess: suspend (T) -> Unit,
): Op.Results<T, Error, Unit> {
    return CollectingErrorsResults(ifSuccess, errorCollection)
}