package com.gojek.gopaysdk.data.exception

interface GopayError {
    fun isNetworkFailure(): Boolean
    fun isServerUnavailable(): Boolean
    fun isRateLimitFailure(): Boolean
}