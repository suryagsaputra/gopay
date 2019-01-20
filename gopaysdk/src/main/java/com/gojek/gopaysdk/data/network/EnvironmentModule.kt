package com.gojek.sdk.data.network

import android.content.Context
import com.gojek.gopayapp.data.network.Environment
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class EnvironmentModule {
    // TODO change with string resource from context refer to go-resto
    @Provides
    @Singleton
    @Named("Latte")
    fun provideLatteEnvironment(context: Context): Environment {
        return Environment(baseUrl = "https://jsonplaceholder.typicode.com/")
    }
}