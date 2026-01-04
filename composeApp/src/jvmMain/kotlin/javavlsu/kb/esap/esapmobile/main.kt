package javavlsu.kb.esap.esapmobile

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import javavlsu.kb.esap.esapmobile.core.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "multiplatform_demo",
    ) {
        App()
    }
}