package cdu278.mangotest.op

class FlatMapOp<A, B, ErrorA, ErrorB>(
    private val original: Op<A, ErrorA>,
    private val transform: suspend (A) -> Op<B, ErrorB>,
    private val transformError: suspend (ErrorA) -> Op<B, ErrorB>,
) : Op<B, ErrorB> {

    override suspend fun <R> perform(results: Op.Results<B, ErrorB, R>): R {
        return original.fold(
            ifSuccess = transform,
            ifFailure = transformError
        ).perform(results)
    }
}

fun <A, B, ErrorA, ErrorB> Op<A, ErrorA>.flatMap(
    transform: suspend (A) -> Op<B, ErrorB>,
    transformError: suspend (ErrorA) -> Op<B, ErrorB>,
): Op<B, ErrorB> {
    return FlatMapOp(original = this, transform, transformError)
}

fun <A, B, ErrorA, ErrorB> Op<A, ErrorA>.map(
    transform: suspend (A) -> B,
    transformError: suspend (ErrorA) -> ErrorB,
): Op<B, ErrorB> {
    return flatMap(
        transform = { Op.Success(transform(it)) },
        transformError = { Op.Failure(transformError(it)) }
    )
}

fun <T, ErrorA, ErrorB> Op<T, ErrorA>.mapError(
    transform: suspend (ErrorA) -> ErrorB,
): Op<T, ErrorB> {
    return map(
        transform = { it },
        transformError = transform,
    )
}