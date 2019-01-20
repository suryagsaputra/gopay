package com.gojek.gopayapp.data.network.latte

import com.gojek.gopayapp.data.network.Environment
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class LatteModule {

    @Provides
    @Singleton
    @Named("AuthorizedLatte")
    fun provideAuthLatteRetrofit(
        @Named("AuthorizedLatte") okHttpClient: OkHttpClient,
        @Named("Latte") environment: Environment,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .client(okHttpClient)
            .baseUrl(environment.baseUrl)
            .build()
    }


}