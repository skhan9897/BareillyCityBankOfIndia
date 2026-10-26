package com.bank.bareillycitybankofindia.app

import android.app.Application

class BareillyBankApp : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: BareillyBankApp
            private set
    }
}
