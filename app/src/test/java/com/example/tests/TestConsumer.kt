package com.example.tests

class TestConsumer : Consumer<String> {

    val _resources = mutableListOf<String>()
    val resources: List<String> = _resources
    val lastResource: String?
        get() = _resources.lastOrNull()
    val invokeCount: Int
        get() = _resources.size

    override fun invoke(resource: String) {
        _resources.add(resource)
    }
}