package javavlsu.kb.esap.esapmobile.presentation.theme

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

@Composable
actual fun getColorScheme(
    darkTheme: Boolean,
    dynamicColor: Boolean
): ColorScheme {
    val context = LocalContext.current

    return when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
}

@Composable
actual fun PlatformThemeSideEffects(
    darkTheme: Boolean,
    colorScheme: ColorScheme
) {
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val activity = (view.context as Activity)
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
            activity.window.statusBarColor = colorScheme.primary.toArgb()
            val insetsController = WindowCompat.getInsetsController(activity.window, view)
            insetsController.hide(WindowInsetsCompat.Type.navigationBars())
            insetsController.isAppearanceLightStatusBars = !darkTheme
        }
    }
}