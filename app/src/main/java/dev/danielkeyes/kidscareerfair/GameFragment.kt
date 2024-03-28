package dev.danielkeyes.kidscareerfair

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import dev.danielkeyes.kidscareerfair.theme.KidsCareerFairTheme

class GameFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        return ComposeView(requireContext()).apply {
            setContent {
                val context = LocalContext.current

                KidsCareerFairTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                    ) {
                        Text(text = "Website Fragment")
                    }
                }
            }
        }
    }
}

// TODO maybe reuse this to have button type ball game?
@Composable
fun RandomBounceButton(
    onClick: () -> Unit,
    buttonText: String,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 400f,
        animationSpec = infiniteRepeatable(tween(2000), RepeatMode.Reverse),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
//            .height(200.dp) // Adjust height as needed
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .height(50.dp) // Adjust button height if needed
                .offset(y = scale.dp)
        ) {
            Text(String.format("%.2f", scale))
        }
    }
}