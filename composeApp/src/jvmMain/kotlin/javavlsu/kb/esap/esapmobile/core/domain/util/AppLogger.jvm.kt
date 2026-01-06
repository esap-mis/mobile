package javavlsu.kb.esap.esapmobile.core.domain.util

import org.koin.dsl.module
import java.util.logging.Level
import java.util.logging.Logger

class DesktopLogger : AppLogger {
    private val logger = Logger.getLogger(this::class.java.name)

    init {
        logger.level = Level.ALL
    }

    override fun v(tag: String, message: String) = logger.finest("[$tag] $message")

    override fun d(tag: String, message: String) = logger.fine("[$tag] $message")

    override fun i(tag: String, message: String) = logger.info("[$tag] $message")

    override fun w(tag: String, message: String) = logger.warning("[$tag] $message")

    override fun e(tag: String, message: String) = logger.severe("[$tag] $message")

    override fun e(tag: String, message: String, throwable: Throwable) =
        logger.log(Level.SEVERE, "[$tag] $message", throwable)
}

actual val appLoggerModule = module {
    single<AppLogger> { DesktopLogger() }
}