package com.gojek.gopaysdk.data.rest

import com.gojek.gopaysdk.data.rest.response.TestRestApiResponse
import retrofit2.http.GET
import rx.Observable

interface LatteApi {

    // Based on https://jsonplaceholder.typicode.com/
    @GET("todos/1")
    fun testRestApi(): Observable<TestRestApiResponse>
}