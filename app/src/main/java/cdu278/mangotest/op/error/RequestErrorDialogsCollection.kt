package cdu278.mangotest.op.error

import cdu278.mangotest.http.ValidatedRequestError
import cdu278.mangotest.ui.error.request.RequestErrorDialogUi
import kotlinx.coroutines.flow.MutableStateFlow

class RequestErrorDialogsCollection(
    private val dialogFlow: MutableStateFlow<RequestErrorDialogUi?>,
) : OpErrorCollection<ValidatedRequestError> {

    override fun add(error: ValidatedRequestError) {
        dialogFlow.value = RequestErrorDialogUi.create(error)
    }
}

fun asDialogs(
    flow: MutableStateFlow<RequestErrorDialogUi?>
): OpErrorCollection<ValidatedRequestError> {
    return RequestErrorDialogsCollection(flow)
}