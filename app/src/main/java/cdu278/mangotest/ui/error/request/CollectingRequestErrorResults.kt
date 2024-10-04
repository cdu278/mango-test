package cdu278.mangotest.ui.error.request

import cdu278.mangotest.http.ValidatedRequestError
import cdu278.mangotest.op.Op
import kotlinx.coroutines.flow.MutableStateFlow

class CollectingRequestErrorResults<T>(
    private val dialogModelFlow: MutableStateFlow<RequestErrorDialogUi?>,
    private val ifSuccess: suspend (T) -> Unit,
) : Op.Results<T, ValidatedRequestError, Unit> {

    override suspend fun success(value: T) {
        ifSuccess(value)
    }

    override suspend fun failure(error: ValidatedRequestError) {
        dialogModelFlow.value = RequestErrorDialogUi.create(error)
    }
}

fun <T> collectingError(
    dialogModelFlow: MutableStateFlow<RequestErrorDialogUi?>,
    ifSuccess: suspend (T) -> Unit
): Op.Results<T, ValidatedRequestError, Unit> {
    return CollectingRequestErrorResults(dialogModelFlow, ifSuccess)
}