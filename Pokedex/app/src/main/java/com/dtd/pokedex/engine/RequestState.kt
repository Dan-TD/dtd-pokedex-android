package com.dtd.pokedex.engine

sealed class RequestState<T> {
    class Loading<T>: RequestState<T>()
    data class Success<T>(val data: T): RequestState<T>()
    data class Failure<T>(val message: String?): RequestState<T>()
}