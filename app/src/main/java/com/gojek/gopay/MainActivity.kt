package com.gojek.gopay

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.gojek.gopayapp.dependency.DaggerSdkComponent

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DaggerSdkComponent
            .builder()
            .context(applicationContext)
            .build()
            .inject(this)
    }
}
