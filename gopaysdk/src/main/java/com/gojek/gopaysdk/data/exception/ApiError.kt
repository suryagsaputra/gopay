package com.gojek.gopaysdk.data.exception

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.adapter.rxjava.HttpException
import java.io.IOException
import java.net.HttpURLConnection
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

class ApiError(val error: Throwable?) : Throwable(error), GopayError {

    companion object {
        const val DEFAULT_ERROR_TITLE = "Istirahat duluuu"
        const val DEFAULT_NETWORK_ERROR_MESSAGE = "Cek koneksi wi-fi atau kuota internetmu dan coba lagi ya."
        const val DEFAULT_ERROR_MESSAGE =
            "Terlalu banyak nomor HP yang telah Anda masukkan. Coba lagi dalam 15 menit, ya."
    }

    private lateinit var response: Response<*>

    fun isAuthFailure(): Boolean = error is HttpException &&
            error.code() == HttpURLConnection.HTTP_UNAUTHORIZED

    fun isValidationFailure(): Boolean = error is HttpException &&
            error.code() == HttpURLConnection.HTTP_BAD_REQUEST

    fun isInternalFailure(): Boolean = error is HttpException &&
            error.code() == HttpURLConnection.HTTP_INTERNAL_ERROR

    override fun isNetworkFailure(): Boolean = error is IOException ||
            error is UnknownHostException ||
            error is TimeoutException

    override fun isServerUnavailable(): Boolean = error is HttpException &&
            (error.code() == HttpURLConnection.HTTP_UNAVAILABLE ||
                    error.code() == HttpURLConnection.HTTP_INTERNAL_ERROR)

    fun getErrorMessageTitle(): String {
        return getErrorResponse().messageTitle ?: DEFAULT_ERROR_TITLE
    }

    override fun isRateLimitFailure(): Boolean = error is HttpException && error.code() == 429

    fun getErrorMessage(): String {
        return getErrorResponse().message ?: DEFAULT_ERROR_MESSAGE
    }

    private fun getErrorResponse(): SignInErrorDetail {
        if (error is IOException) return SignInErrorDetail("", DEFAULT_NETWORK_ERROR_MESSAGE, "", "")
        if (error is HttpException) response = error.response()

        val errorResponse = getErrorBody(SignInErrorModel::class.java)
        if (errorResponse?.errors != null && errorResponse.errors.isNotEmpty()) {
            return SignInErrorDetail(
                errorResponse.errors[0].code,
                errorResponse.errors[0].message,
                errorResponse.errors[0].messageTitle,
                errorResponse.errors[0].messageSeverity
            )

        } else if (errorResponse?.message != null) {
            return SignInErrorDetail("", errorResponse.message, "", "")
        }

        return SignInErrorDetail("", DEFAULT_ERROR_MESSAGE, DEFAULT_ERROR_TITLE, "")
    }

    private fun <T> getErrorBody(clazz: Class<T>): T? {
        return try {
            val jsonString = response.errorBody().string()
            Gson().fromJson(jsonString, clazz)
        } catch (e: Exception) {
            null
        }
    }
}

data class SignInErrorModel(
    @SerializedName("success") val success: Boolean,
    @SerializedName("errors") val errors: List<SignInErrorDetail>?,
    @SerializedName("message") val message: String?
)

data class SignInErrorDetail(
    @SerializedName("code") val code: String?,
    @SerializedName("message") val message: String?,
    @SerializedName("message_title") val messageTitle: String?,
    @SerializedName("message_severity") val messageSeverity: String?
)