package com.example.tests

import org.junit.Assert.assertEquals
import org.junit.Test

class ResourceManagerTest {

    @Test
    fun consumeResourceAfterSetResourceCallReceivesResource() {
        //arrange
        val resourceManager = createResourceManager()

        val consumer = TestConsumer()

        //act
        resourceManager.setResource("TEST")
        resourceManager.consumeResource(consumer)

        //assert
        assertEquals("TEST", consumer.lastResource)
        assertEquals(1, consumer.invokeCount)
    }


    @Test
    fun consumeResourceCallsAfterSetResourceCallReceivesResourceEachConsumer() {
        //arrange
        val resourceManager = createResourceManager()

        val consumer1 = TestConsumer()
        val consumer2 = TestConsumer()

        //act
        resourceManager.setResource("TEST")
        resourceManager.consumeResource(consumer1)
        resourceManager.consumeResource(consumer2)


        //assert
        assertEquals("TEST", consumer1.lastResource)
        assertEquals(1, consumer1.invokeCount)
        assertEquals("TEST", consumer2.lastResource)
        assertEquals(1, consumer2.invokeCount)
    }

    @Test
    fun consumeResourceAfterSetResourceCallsReceiveLatestResource() {
        val resourceManager = createResourceManager()

        val consumer = TestConsumer()

        resourceManager.setResource("TEST1")
        resourceManager.setResource("TEST2")
        resourceManager.consumeResource(consumer)

        assertEquals("TEST2", consumer.lastResource)
        assertEquals(1, consumer.invokeCount)
    }

    @Test
    fun consumeResourceCallsWithSameConsumerCanReceiveTheSameResource() {
        val resourceManager = createResourceManager()

        val consumer = TestConsumer()

        resourceManager.setResource("TEST")
        resourceManager.consumeResource(consumer)
        resourceManager.consumeResource(consumer)

        assertEquals("TEST", consumer._resources[0])
        assertEquals("TEST", consumer._resources[1])
        assertEquals(2, consumer.invokeCount)
    }

    @Test
    fun consumeResourceWithoutActiveResourceDoesNothing() {
        val resourceManager = createResourceManager()

        val consumer = TestConsumer()

        assertEquals(0, consumer.invokeCount)
    }

    @Test
    fun setResourceAfterConsumeResourceCallDeliversResourceToConsumer() {
        val resourceManager = createResourceManager()

        val consumer = TestConsumer()

        resourceManager.consumeResource(consumer)
        resourceManager.setResource("TEST")

        assertEquals("TEST", consumer.lastResource)
        assertEquals(1, consumer.invokeCount)
    }

    private fun createResourceManager(
        testExecutor: TestExecutor = TestExecutor(),
        testErrorHandler: TestErrorHandler = TestErrorHandler()
    ): ResourceManager<String> {
        return ResourceManager(
            testExecutor, testErrorHandler
        )

    }


}