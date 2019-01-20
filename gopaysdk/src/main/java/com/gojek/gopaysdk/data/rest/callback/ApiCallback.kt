package com.gojek.gopaysdk.data.rest.callback

import com.gojek.gopaysdk.data.exception.ApiError

interface ApiCallback<T> {

    fun onSuccess(response: T?)

    fun onError(error: ApiError)
}