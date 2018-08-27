package com.monmaru.fp.monamona

sealed class Option<out T>
data class Some<out T>(val value: T) : Option<T>()
object None : Option<Nothing>()

fun <T, R> Option<T>.map(func: (T) -> R): Option<R> {
    return when(this) {
        is Some<T> -> Some(func(this.value))
        is None -> None
    }
}

fun <T, R> Option<T>.flatMap(func: (T) -> Option<R>): Option<R> {
    return when(this) {
        is Some<T> -> func(this.value)
        is None -> None
    }
}
