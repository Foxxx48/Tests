package com.example.tests

import org.junit.Test

class ResourceManagerTest {

    @Test
    fun consumeResourceAfterSetResourceCallReceivesResource() {
        //arrange
            val resourceManager = ResourceManager<String>(
                TestExecutor(), TestErrorHandler()
            )
        //act

        //assert
    }
}