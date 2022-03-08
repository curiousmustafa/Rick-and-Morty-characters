package com.example.apollographqlplayground

sealed class Response<T>(
    var data: T? = null,
    var message: String? = null,
) {
    class Success<T>(data: T) : Response<T>(data = data)
    class Error<T>(message: String) : Response<T>(message = message)
}