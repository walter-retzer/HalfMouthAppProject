package network


sealed class ResultNetwork<out T> {
    data class Success<out T>(val data: T) : ResultNetwork<T>()
    data class Failure(val exception: Throwable) : ResultNetwork<Nothing>()

    companion object {
        fun <T> success(data: T): ResultNetwork<T> = Success(data)
        fun failure(exception: Throwable): ResultNetwork<Nothing> = Failure(exception)
    }
}
