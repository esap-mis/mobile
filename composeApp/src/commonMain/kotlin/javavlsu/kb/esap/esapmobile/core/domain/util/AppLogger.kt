package javavlsu.kb.esap.esapmobile.core.domain.util

import org.koin.core.module.Module

interface AppLogger {
    fun v(tag: String, message: String)
    fun d(tag: String, message: String)
    fun i(tag: String, message: String)
    fun w(tag: String, message: String)
    fun e(tag: String, message: String)
    fun e(tag: String, message: String, throwable: Throwable)
}

expect val appLoggerModule: Module