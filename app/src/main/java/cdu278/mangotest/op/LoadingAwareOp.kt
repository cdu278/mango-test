package cdu278.mangotest.op

import kotlinx.coroutines.flow.MutableStateFlow

class LoadingAwareOp<T, Error>(
    private val original: Op<T, Error>,
    private val loadingFlow: MutableStateFlow<Boolean>,
) : Op<T, Error> {

    override suspend fun <R> perform(results: Op.Results<T, Error, R>): R {
        return try {
            loadingFlow.value = true
            original.perform(results)
        } finally {
            loadingFlow.value = false
        }
    }
}

fun <T, Error> Op<T, Error>.loadingAware(loadingFlow: MutableStateFlow<Boolean>): Op<T, Error> {
    return LoadingAwareOp(original = this, loadingFlow)
}