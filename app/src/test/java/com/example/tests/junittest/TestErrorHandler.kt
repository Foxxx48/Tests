package com.example.tests.junittest

import com.example.tests.ErrorHandler

class TestErrorHandler : ErrorHandler<String> {
    private val _records = mutableListOf<Record>()
    val records: List<Record> = _records
    val invokeCount: Int get() = records.size

    override fun onError(exception: Exception, resource: String) {
        _records.add(Record(exception, resource))
    }

    data class Record(
        val exception: Exception,
        val resource: String
    )

}