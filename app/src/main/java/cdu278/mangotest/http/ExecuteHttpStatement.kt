package cdu278.mangotest.http

import cdu278.mangotest.op.Op
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.HttpStatement
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExecuteHttpStatement(
    private val statement: suspend () -> HttpStatement,
) : Op<HttpResponse, HttpError> {

    constructor(statement: HttpStatement) : this({ statement })

    override suspend fun <R> perform(results: Op.Results<HttpResponse, HttpError, R>): R {
        return withContext(Dispatchers.IO) {
            try {
                val response = statement().execute()
                if (response.status.isSuccess()) {
                    results.success(response)
                } else {
                    results.failure(HttpError.Failure(response))
                }
            } catch (e: Exception) {
                results.failure(HttpError.Io(e))
            }
        }
    }
}