package javavlsu.kb.esap.esapmobile

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import javavlsu.kb.esap.esapmobile.App
import javavlsu.kb.esap.esapmobile.core.di.appModules
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level

fun main() = application {
    startKoin {
        modules(appModules)
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "ESAP Desktop",
    ) {
        App()
    }
}