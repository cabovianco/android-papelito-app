package com.cabovianco.papelito.domain.model

sealed class AppError {
    sealed class DatabaseError : AppError() {
        data object NotFound : DatabaseError()
        data object Unknown : DatabaseError()
    }

    data object InvalidNoteParametersError : AppError()
}

sealed class Result<out T> {
    data class Ok<T>(val value: T) : Result<T>()
    data class Err(val value: AppError) : Result<Nothing>()
}

fun <T> ok(value: T): Result<T> = Result.Ok(value)

fun err(value: AppError): Result<Nothing> = Result.Err(value)

fun <T> Result<T>.isOk(): Boolean = this is Result.Ok

fun <T> Result<T>.isErr(): Boolean = this is Result.Err

fun <T> Result<T>.getOrElse(default: T): T = when (this) {
    is Result.Ok -> this.value
    is Result.Err -> default
}

fun <T> Result<T>.unwrap(): T = when (this) {
    is Result.Ok -> this.value
    is Result.Err -> throw IllegalStateException()
}

fun <T> Result<T>.unwrapErr(): AppError = when (this) {
    is Result.Ok -> throw IllegalStateException()
    is Result.Err -> this.value
}

suspend fun <T> Result<T>.onOk(block: suspend (T) -> Unit): Result<T> {
    if (this is Result.Ok) {
        block(this.value)
    }

    return this
}

suspend fun <T> Result<T>.onErr(block: suspend (AppError) -> Unit): Result<T> {
    if (this is Result.Err) {
        block(this.value)
    }

    return this
}
