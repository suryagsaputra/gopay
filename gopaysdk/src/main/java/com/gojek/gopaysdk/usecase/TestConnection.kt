package com.gojek.gopaysdk.usecase

import com.gojek.gopaysdk.data.exception.ApiError
import com.gojek.gopaysdk.data.rest.LatteService
import com.gojek.gopaysdk.data.rest.callback.ApiCallback
import com.gojek.gopaysdk.data.rest.response.TestRestApiResponse
import rx.Subscription
import javax.inject.Inject

class TestConnection @Inject constructor(private val service: LatteService) {

    fun testRestApiConnection(callback: DoTestRestApiCallback): Subscription {
        return service.testRestApi(object : ApiCallback<TestRestApiResponse> {
            override fun onSuccess(response: TestRestApiResponse?) {
                response?.let { callback.onSuccess(it) }
            }

            override fun onError(error: ApiError) {
                callback.onFailure(error)
            }

        })
    }

    interface DoTestRestApiCallback {
        fun onSuccess(testRestApiResponse: TestRestApiResponse)
        fun onFailure(error: ApiError)
    }
}