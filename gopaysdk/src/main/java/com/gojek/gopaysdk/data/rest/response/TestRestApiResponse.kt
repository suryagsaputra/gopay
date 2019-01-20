package com.gojek.gopaysdk.data.rest.response

import com.google.gson.annotations.SerializedName

// Based on https://jsonplaceholder.typicode.com/
data class TestRestApiResponse(@SerializedName("completed") val completed: Boolean)