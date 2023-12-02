package com.linkandroid

import android.app.Application
import utils.onInitializeDebugBuild

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        onInitializeDebugBuild()
    }
}