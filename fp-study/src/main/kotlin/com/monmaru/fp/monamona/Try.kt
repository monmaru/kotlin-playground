package com.monmaru.fp.monamona

sealed class Try<out T> {
    companion object {
        fun <T> wrap(func: () -> T): Try<T> {
            return try {
                Success(func())
            } catch (e: Throwable) {
                Failure(e)
            }
        }
    }
}

data class Success<out T>(val value: T) : Try<T>()
data class Failure<out T>(val error: Throwable) : Try<T>()


fun <T> Try<T>.invoke(func: () -> T): Try<T> {
    return try {
        Success(func())
    } catch (e: Throwable) {
        Failure(e)
    }
}

fun <T, R> Try<T>.map(func: (T) -> R): Try<R> {
    return when(this) {
        is Success<T> -> Success(func(this.value))
        is Failure -> Failure(this.error)
    }
}

fun <T, R> Try<T>.flatMap(func: (T) -> Try<R>): Try<R> {
    return when(this) {
        is Success<T> -> func(this.value)
        is Failure -> Failure(this.error)
    }
}

