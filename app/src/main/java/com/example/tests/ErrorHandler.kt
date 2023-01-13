package com.example.tests

interface ErrorHandler<R> {
    fun onError(exception: Exception, resource: R)
}