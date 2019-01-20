package com.gojek.gopayapp.dependency

import android.content.Context
import com.gojek.gopay.MainActivity
import com.gojek.gopayapp.data.network.latte.LatteModule
import com.gojek.gopaysdk.data.network.NetworkModule
import com.gojek.sdk.data.network.EnvironmentModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        LatteModule::class,
        EnvironmentModule::class,
        NetworkModule::class
    ]
)
interface SdkComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder

        fun build(): SdkComponent
    }

    fun inject(app: MainActivity)
}