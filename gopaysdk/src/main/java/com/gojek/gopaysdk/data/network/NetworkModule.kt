package com.gojek.gopaysdk.data.network

import android.content.Context
import com.gojek.gopaysdk.BuildConfig
import com.gojek.gopaysdk.R
import com.grapesnberries.curllogger.CurlLoggerInterceptor
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun providesCurlLoggerInterceptor(): CurlLoggerInterceptor {
        return CurlLoggerInterceptor()
    }

    @Provides
    @Singleton
    fun providesHttpLoggingInterceptor(context: Context): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = getOkHttpLogLevel(context.getString(R.string.okhttp_log_level))
        }
    }

    private fun getOkHttpLogLevel(level: String?): HttpLoggingInterceptor.Level {
        return when (level) {
            HttpLoggingInterceptor.Level.NONE.toString() -> HttpLoggingInterceptor.Level.NONE
            HttpLoggingInterceptor.Level.BASIC.toString() -> HttpLoggingInterceptor.Level.BASIC
            HttpLoggingInterceptor.Level.HEADERS.toString() -> HttpLoggingInterceptor.Level.HEADERS
            HttpLoggingInterceptor.Level.BODY.toString() -> HttpLoggingInterceptor.Level.BODY
            else -> HttpLoggingInterceptor.Level.NONE
        }
    }

    @Provides
    @Singleton
    fun providesChuckInterceptor(context: Context): ChuckInterceptor = ChuckInterceptor(context)

    @Provides
    @Singleton
    fun providesConnectionPool(): ConnectionPool =
        ConnectionPool(MAX_IDLE_CONNECTIONS, KEEP_ALIVE_DURATION, TimeUnit.MILLISECONDS)

    @Provides
    @Singleton
    fun providesCache(context: Context): Cache =
        Cache(context.externalCacheDir, CACHE_DISK_SIZE_30MB.toLong())

    @Provides
    @Singleton
    fun providesOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        curlLoggerInterceptor: CurlLoggerInterceptor,
        chuckInterceptor: ChuckInterceptor,
        connectionPool: ConnectionPool,
        cache: Cache
    ): OkHttpClient {

        val builder = OkHttpClient.Builder()
        builder.cache(cache)
        builder.connectionPool(connectionPool)
        builder.readTimeout(READ_TIME_OUT.toLong(), TimeUnit.SECONDS)
        builder.writeTimeout(WRITE_TIME_OUT.toLong(), TimeUnit.SECONDS)
        builder.connectTimeout(CONNECTION_TIME_OUT.toLong(), TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(httpLoggingInterceptor)
            builder.addInterceptor(curlLoggerInterceptor)
            builder.addInterceptor(chuckInterceptor)
        }
        return builder.build()
    }

    companion object {
        private const val CACHE_DISK_SIZE_30MB = 30 * 1024 * 1024
        private const val READ_TIME_OUT = 120
        private const val WRITE_TIME_OUT = 120
        private const val CONNECTION_TIME_OUT = 30
        private const val KEEP_ALIVE_DURATION = (30 * 1000).toLong()
        private const val MAX_IDLE_CONNECTIONS = 10
    }
}