package network


sealed class ResultType<out T> {
    data class Success<out T>(val data: T) : ResultType<T>()
    data class Failure(val exception: Throwable) : ResultType<Nothing>()

    companion object {
        fun <T> success(data: T): ResultType<T> = Success(data)
        fun failure(exception: Throwable): ResultType<Nothing> = Failure(exception)
    }
}
