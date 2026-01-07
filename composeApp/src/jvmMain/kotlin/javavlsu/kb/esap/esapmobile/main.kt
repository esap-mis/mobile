package javavlsu.kb.esap.esapmobile

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import esapmobile.composeapp.generated.resources.Res
import esapmobile.composeapp.generated.resources.app_name
import javavlsu.kb.esap.esapmobile.core.di.appModules
import org.jetbrains.compose.resources.stringResource
import org.koin.core.context.GlobalContext.startKoin

fun main() = application {
    startKoin {
        modules(appModules)
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = stringResource(Res.string.app_name),
        state = WindowState(
            position = WindowPosition(250.dp, 10.dp),
            size = DpSize(500.dp, 700.dp)),
        resizable = false,
    ) {
        App()
    }
}