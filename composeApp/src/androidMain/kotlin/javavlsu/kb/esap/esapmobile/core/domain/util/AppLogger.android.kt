package javavlsu.kb.esap.esapmobile.core.domain.util

import android.util.Log
import org.koin.core.module.Module
import org.koin.dsl.module

class AndroidLogger : AppLogger {
    override fun v(tag: String, message: String) {
        Log.v(tag, message)
    }

    override fun d(tag: String, message: String) {
        Log.d(tag, message)
    }

    override fun i(tag: String, message: String) {
        Log.i(tag, message)
    }

    override fun w(tag: String, message: String) {
        Log.w(tag, message)
    }

    override fun e(tag: String, message: String) {
        Log.e(tag, message)
    }

    override fun e(tag: String, message: String, throwable: Throwable) {
        Log.e(tag, message, throwable)
    }
}

actual val appLoggerModule: Module = module {
    single<AppLogger> { AndroidLogger() }
}