package com.example.tests.mockktest

import com.example.tests.Consumer
import com.example.tests.ErrorHandler
import com.example.tests.ResourceManager
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.concurrent.Executor

class ResourceManagerMockKTest {

    @Test
    fun consumeResourceAfterSetResourceCallReceivesResource() {
        //arrange
        val resourceManager = createResourceManager()

        val consumer = createConsumer()

        //act
        resourceManager.setResource("TEST")
        resourceManager.consumeResource(consumer)

        //assert
        verify(exactly = 1) {
            consumer("TEST")
        }
        confirmVerified(consumer)
    }


    @Test
    fun consumeResourceCallsAfterSetResourceCallReceivesResourceEachConsumer() {
        //arrange
        val resourceManager = createResourceManager()

        val consumer1 = createConsumer()
        val consumer2 = createConsumer()

        //act
        resourceManager.setResource("TEST")
        resourceManager.consumeResource(consumer1)
        resourceManager.consumeResource(consumer2)


        //assert

        verifySequence {
            consumer1("TEST")
            consumer2("TEST")
        }

    }

    @Test
    fun consumeResourceAfterSetResourceCallsReceiveLatestResource() {
        val resourceManager = createResourceManager()

        val consumer = createConsumer()

        resourceManager.setResource("TEST1")
        resourceManager.setResource("TEST2")
        resourceManager.consumeResource(consumer)

        verify(exactly = 1) {
            consumer("TEST2")
        }
        confirmVerified(consumer)
    }

    @Test
    fun consumeResourceCallsWithSameConsumerCanReceiveTheSameResource() {
        val resourceManager = createResourceManager()

        val consumer = createConsumer()

        resourceManager.setResource("TEST")
        resourceManager.consumeResource(consumer)
        resourceManager.consumeResource(consumer)

        verify(exactly = 2) {
            consumer("TEST")
        }
        confirmVerified(consumer)
    }

    @Test
    fun consumeResourceWithoutActiveResourceDoesNothing() {
        val resourceManager = createResourceManager()

        val consumer = createConsumer()

        verify() {
            consumer wasNot called
        }
    }

    @Test
    fun setResourceAfterConsumeResourceCallDeliversResourceToConsumer() {
        val resourceManager = createResourceManager()

        val consumer = createConsumer()

        resourceManager.consumeResource(consumer)
        resourceManager.setResource("TEST")

        verify(exactly = 1) {
            consumer("TEST")
        }
        confirmVerified(consumer)
    }

    /*@Test
    fun consumeResourceReceivesResourceOnlyOnce() {
        val resourceManager = createResourceManager()
        val consumer = TestMockKConsumer()

        resourceManager.setResource("TEST1")
        resourceManager.consumeResource(consumer)
        resourceManager.setResource("TEST2")

        assertEquals("TEST1", consumer.lastResource)
        assertEquals(1, consumer.invokeCount)
    }

    @Test
    fun consumeResourceCallsWithSameConsumerCanReceiveMultipleResources() {
        val resourceManager = createResourceManager()
        val consumer = TestMockKConsumer()

        resourceManager.setResource("TEST1")
        resourceManager.consumeResource(consumer)
        resourceManager.setResource("TEST2")
        resourceManager.consumeResource(consumer)

        assertEquals("TEST1", consumer.resources[0])
        assertEquals("TEST2", consumer.resources[1])
        assertEquals(2, consumer.invokeCount)
    }

    @Test
    fun setResourceAfterMultipleConsumeResourceCallsDeliversResourceToAllConsumers() {
        val resourceManager = createResourceManager()
        val consumer1 = TestMockKConsumer()
        val consumer2 = TestMockKConsumer()

        resourceManager.consumeResource(consumer1)
        resourceManager.consumeResource(consumer2)
        resourceManager.setResource("TEST")

        assertEquals("TEST", consumer1.lastResource)
        assertEquals("TEST", consumer2.lastResource)
        assertEquals(1, consumer1.invokeCount)
        assertEquals(1, consumer2.invokeCount)
    }

    @Test
    fun setResourceCallsAfterConsumeResourceCallDeliversTheFirstResourceOnce() {
        val resourceManager = createResourceManager()
        val consumer = TestMockKConsumer()

        resourceManager.consumeResource(consumer)
        resourceManager.setResource("TEST1")
        resourceManager.setResource("TEST2")

        assertEquals(1, consumer.invokeCount)
        assertEquals("TEST1", consumer.lastResource)
    }

    @Test
    fun setResourceBetweenConsumeResourceCallsDeliversTheSameResourceToAllConsumers() {
        val resourceManager = createResourceManager()
        val consumer = TestMockKConsumer()

        resourceManager.consumeResource(consumer)
        resourceManager.setResource("TEST")
        resourceManager.consumeResource(consumer)

        assertEquals(2, consumer.invokeCount)
        assertEquals("TEST", consumer.resources[0])
        assertEquals("TEST", consumer.resources[1])
    }

    @Test
    fun setResourceDoubleCallBetweenConsumeResourceCallsDeliversDifferentResources() {
        val resourceManager = createResourceManager()
        val consumer = TestMockKConsumer()

        resourceManager.consumeResource(consumer)
        resourceManager.setResource("TEST1")
        resourceManager.setResource("TEST2")
        resourceManager.consumeResource(consumer)

        assertEquals(2, consumer.invokeCount)
        assertEquals("TEST1", consumer.resources[0])
        assertEquals("TEST2", consumer.resources[1])
    }

    @Test
    fun consumeResourceAfterClearResourceCallDoesNothing() {
        val resourceManager = createResourceManager()
        val consumer = TestMockKConsumer()

        resourceManager.setResource("TEST")
        resourceManager.clearResource()
        resourceManager.consumeResource(consumer)

        assertEquals(0, consumer.invokeCount)
    }

    @Test
    fun consumeResourceAfterClearResourceAndSetResourceCallsReceivesLatestResource() {
        val resourceManager = createResourceManager()
        val consumer = TestMockKConsumer()

        resourceManager.setResource("TEST1")
        resourceManager.clearResource()
        resourceManager.setResource("TEST2")
        resourceManager.consumeResource(consumer)

        assertEquals(1, consumer.invokeCount)
        assertEquals("TEST2", consumer.lastResource)
    }

    @Test
    fun setResourceAfterConsumeResourceAndClearResourceCallsDeliversLatestResource() {
        val resourceManager = createResourceManager()
        val consumer = TestMockKConsumer()

        resourceManager.setResource("TEST1")
        resourceManager.clearResource()
        resourceManager.consumeResource(consumer)
        resourceManager.setResource("TEST2")

        assertEquals(1, consumer.invokeCount)
        assertEquals("TEST2", consumer.lastResource)
    }

    @Test
    fun destroyClearsCurrentResource() {
        val resourceManager = createResourceManager()
        val consumer = TestMockKConsumer()

        resourceManager.setResource("TEST")
        resourceManager.destroy()
        resourceManager.consumeResource(consumer)

        assertEquals(0, consumer.invokeCount)
    }

    @Test
    fun destroyClearsPendingConsumers() {
        val resourceManager = createResourceManager()
        val consumer = TestMockKConsumer()

        resourceManager.consumeResource(consumer)
        resourceManager.destroy()
        resourceManager.setResource("TEST")

        assertEquals(0, consumer.invokeCount)
    }

    @Test
    fun setResourceAfterDestroyCallDoesNothing() {
        val resourceManager = createResourceManager()
        val consumer = TestMockKConsumer()

        resourceManager.destroy()
        resourceManager.setResource("TEST")
        resourceManager.consumeResource(consumer)

        assertEquals(0, consumer.invokeCount)
    }

    @Test
    fun consumeResourceAfterDestroyCallDoesNothing() {
        val resourceManager = createResourceManager()
        val consumer = TestMockKConsumer()

        resourceManager.destroy()
        resourceManager.consumeResource(consumer)
        resourceManager.setResource("TEST")

        assertEquals(0, consumer.invokeCount)
    }

    @Test(expected = Test.None::class)
    fun setResourceHandlesConcurrentConsumersModification() {
        val resourceManager = createResourceManager()
        val consumer = TestMockKConsumer()

        resourceManager.consumeResource {
            resourceManager.clearResource()
            resourceManager.consumeResource(consumer)
        }
        resourceManager.setResource("TEST")

        assertEquals(1, consumer.invokeCount)
        assertEquals("TEST", consumer.lastResource)
    }

    @Test
    fun consumeResourceDeliversExceptionsToErrorHandler() {
        val errorHandler = TestMockKConsumer()
        val resourceManager = createResourceManager(
            errorHandler = errorHandler
        )
        val expectedException = IllegalStateException("Test exception")

        resourceManager.setResource("TEST")
        resourceManager.consumeResource { resource ->
            throw expectedException
        }

        assertEquals(1, errorHandler.invokeCount)
        assertEquals(
            TestErrorHandler.Record(expectedException, "TEST"),
            errorHandler.records[0]
        )
    }

    @Test
    fun setResourceDeliversExceptionsToErrorHandler() {
        val errorHandler = TestErrorHandler()
        val resourceManager = createResourceManager(
            errorHandler = errorHandler
        )
        val expectedException = IllegalStateException("Test exception")

        resourceManager.consumeResource { resource ->
            throw expectedException
        }
        resourceManager.setResource("TEST")

        assertEquals(1, errorHandler.invokeCount)
        assertEquals(
            TestErrorHandler.Record(expectedException, "TEST"),
            errorHandler.records[0]
        )
    }

    @Test
    fun consumeResourceInvokesConsumerInExecutor() {
        val executor = TestExecutor(autoExec = false)
        val resourceManager = createResourceManager(
            executor = executor
        )
        val consumer = TestConsumer()

        resourceManager.setResource("TEST")
        resourceManager.consumeResource(consumer)

        assertEquals(1, executor.invokeCount)
        assertEquals(0, consumer.invokeCount)
        executor.commands[0].run()
        assertEquals(1, consumer.invokeCount)
        assertEquals("TEST", consumer.lastResource)
    }

    @Test
    fun setResourceInvokesPendingConsumerInExecutor() {
        val executor = TestExecutor(autoExec = false)
        val resourceManager = createResourceManager(
            executor = executor
        )
        val consumer = TestConsumer()

        resourceManager.consumeResource(consumer)
        resourceManager.setResource("TEST")

        assertEquals(1, executor.invokeCount)
        assertEquals(0, consumer.invokeCount)
        executor.commands[0].run()
        assertEquals(1, consumer.invokeCount)
        assertEquals("TEST", consumer.lastResource)
    }*/

    private fun createResourceManager(
        executor: Executor = immediateExecutor(),
        errorHandler: ErrorHandler<String> = dummyErrorHandler()
    ): ResourceManager<String> {
        return ResourceManager(
            executor, errorHandler
        )
    }

    private fun dummyErrorHandler(): ErrorHandler<String> = mockk()

    private fun immediateExecutor(): Executor {
        val executor = mockk<Executor>()
        every { executor.execute(any()) } answers {
            firstArg<Runnable>().run()
        }
        return executor
    }

    private fun createConsumer(): Consumer<String> = mockk(relaxed = true)


}