package com.fut.app.common.api

sealed class APIResult<out Value, out Error> {
    inline infix fun <V> mapSuccess(transform: (Value) -> V): APIResult<V, Error> = when (this) {
        is Success -> Success(transform(value))
        is Failure -> this
    }

    inline fun <E> mapError(transform: (Error) -> E): APIResult<Value, E> = when (this) {
        is Success -> this
        is Failure -> Failure(transform(error))
    }

    inline infix fun onSuccess(action: (Value) -> Unit): APIResult<Value, Error> {
        if (this is Success) action(value)
        return this
    }

    inline fun onFailure(action: (Error) -> Unit): APIResult<Value, Error> {
        if (this is Failure) action(error)
        return this
    }

    data class Success<out Value>(val value: Value) : APIResult<Value, Nothing>()
    data class Failure<out Error>(val error: Error) : APIResult<Nothing, Error>()
}