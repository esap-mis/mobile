package javavlsu.kb.esap.esapmobile

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import esapmobile.composeapp.generated.resources.Res
import esapmobile.composeapp.generated.resources.app_name
import javavlsu.kb.esap.esapmobile.core.di.appModules
import org.jetbrains.compose.resources.stringResource
import org.koin.core.context.GlobalContext.startKoin

fun main()  {
    System.setProperty("skiko.renderApi", "OPENGL")
    System.setProperty("compose.desktop.rendering.multisample", "true")
    System.setProperty("compose.desktop.rendering.vsync", "true")
    System.setProperty("compose.desktop.render.half.pixel.snap", "false")
    System.setProperty("sun.java2d.uiScale.enabled", "true")
    System.setProperty("awt.useSystemAAFontSettings","on")
    System.setProperty("swing.aatext", "true")

    application {
        startKoin {
            modules(appModules)
        }

        Window(
            onCloseRequest = ::exitApplication,
            title = stringResource(Res.string.app_name),
            state = rememberWindowState(),
            resizable = true,
        ) {
            App()
        }
    }
}