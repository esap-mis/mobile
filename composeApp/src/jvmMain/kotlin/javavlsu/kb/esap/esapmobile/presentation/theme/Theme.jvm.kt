package javavlsu.kb.esap.esapmobile.presentation.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect

@Composable
actual fun getColorScheme(
    darkTheme: Boolean,
    dynamicColor: Boolean
): ColorScheme {
    return if (darkTheme) DarkColorScheme else LightColorScheme
}

@Composable
actual fun PlatformThemeSideEffects(
    darkTheme: Boolean,
    colorScheme: ColorScheme
) {
    SideEffect {
    }
}