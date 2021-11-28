import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import ui.App

@OptIn(ExperimentalComposeUiApi::class)
fun main() = application {
    // Look at https://github.com/adrielcafe/lyricist for localization
    Window(
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(width = Dp.Unspecified, height = Dp.Unspecified),
        title = "Polyglot",
        icon = painterResource(R.drawable.language)
    ) {
        App()
    }
}
