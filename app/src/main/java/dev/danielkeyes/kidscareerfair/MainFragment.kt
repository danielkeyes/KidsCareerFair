package dev.danielkeyes.kidscareerfair

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.findNavController
import dev.danielkeyes.kidscareerfair.theme.KidsCareerFairTheme

// TODO implement Play Game and enable by changing navgraph start destination
// Current unused as only "Play Game" is not yet implemented
class MainFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        return ComposeView(requireContext()).apply {
            setContent {
                val navController = findNavController()
                KidsCareerFairTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                    ) {
                        MainMenu(navController
                        ) { url ->
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainMenu(navController: NavController, externalUrlLaunch: (String) -> Unit) {
    Column {
        Button(onClick = { navController.navigate(R.id.action_mainFragment_to_photoFrameFragment) }) {
            Text(text = "Photo Frame")
        }
        Button(onClick = { navController.navigate(R.id.action_mainFragment_to_game_fragment) }) {
            Text(text = "Play Game")
        }
        Button(onClick = {
            val url = "https://www.nisd.net/leonvalley"
            externalUrlLaunch(url)
        }) {
            Text(text = "Open Leon Valley Website")
        }
    } 
}

@Preview(showBackground = true, name = "10-inch Tablet Portrait", widthDp = 600, heightDp = 960)
@Preview(showBackground = true, name = "10-inch Tablet Landscape", widthDp = 960, heightDp = 600)
@Preview(showBackground = true, name = "pixel4", device = "id:pixel_4")
@Preview(showBackground = true, name = "automotive", device = Devices. AUTOMOTIVE_1024p)
@Composable
private fun PreviewMainMenu() {
    KidsCareerFairTheme {
        Surface {
            MainMenu(navController = rememberNavController()) { }
        }
    }
}