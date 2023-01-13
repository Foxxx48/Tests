package com.example.tests

import java.util.concurrent.Executor

class ResourceManager<R>(
    private val executor: Executor,
    private val errorHandler: ErrorHandler<R>
) {

    fun setResource(resource: R) {
        TODO()
    }

    fun clearResource() {
        TODO()
    }

    fun consumeResource(consumer: Consumer<R>) {
        TODO()
    }

    fun destroy() {
        TODO()
    }


}