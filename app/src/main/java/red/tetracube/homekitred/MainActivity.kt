package red.tetracube.homekitred

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.animation.doOnEnd
import red.tetracube.homekitred.ui.shell.ShellScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        splashScreen.setOnExitAnimationListener { splashScreenView ->
            val fadeOut = ObjectAnimator.ofFloat(
                splashScreenView,
                View.ALPHA,
                1f,
                0f
            )
            fadeOut.duration = 600L
            fadeOut.doOnEnd { splashScreenView.remove() }
            fadeOut.start()
        }
        enableEdgeToEdge()
        setContent {
            ShellScreen()
        }
    }
}