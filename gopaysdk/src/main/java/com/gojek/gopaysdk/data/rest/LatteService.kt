package com.gojek.gopaysdk.data.rest

import com.gojek.gopaysdk.data.rest.callback.ApiCallback
import com.gojek.gopaysdk.data.rest.request.execute
import com.gojek.gopaysdk.data.rest.response.TestRestApiResponse
import rx.Subscription

class LatteService(private val api: LatteApi) {

    fun testRestApi(callback: ApiCallback<TestRestApiResponse>): Subscription {
        return execute(callback) {
            api.testRestApi()
        }
    }
}