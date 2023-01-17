package com.example.tests

import java.util.concurrent.Executor

fun main() {
    val resourceManager = ResourceManager<String>(
        executor = ImmediateExecutor(),
        errorHandler = PrintlnErrorHandler()
    )

    resourceManager.consumeResource {
        println("1: $it")
    }
    resourceManager.consumeResource {
        println("2: $it")
    }
    resourceManager.setResource("R1")
    resourceManager.consumeResource {
        println("3: $it")
    }
    resourceManager.setResource("R2")
    resourceManager.consumeResource {
        println("4: $it")
    }

}

class ImmediateExecutor : Executor {
    override fun execute(command: Runnable) {
        command.run()
    }
}

class PrintlnErrorHandler<R> : ErrorHandler<R> {
    override fun onError(exception: Exception, resource: R) {
        println("Error with '$resource': ${exception.message}")
    }
}