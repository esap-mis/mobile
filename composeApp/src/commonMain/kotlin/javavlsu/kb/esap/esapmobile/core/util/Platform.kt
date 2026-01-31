package javavlsu.kb.esap.esapmobile.core.util

enum class Platform {
    Android, Desktop
}

expect fun getPlatform(): Platform