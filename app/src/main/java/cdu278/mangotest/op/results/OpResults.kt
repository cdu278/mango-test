package cdu278.mangotest.op.results

import cdu278.mangotest.op.Op

class OpResults<T, Error, R>(
    private val ifSuccess: suspend (T) -> R,
    private val ifFailure: suspend (Error) -> R,
) : Op.Results<T, Error, R> {

    override suspend fun success(value: T): R = ifSuccess(value)

    override suspend fun failure(error: Error): R = ifFailure(error)
}