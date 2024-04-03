package dev.danielkeyes.kidscareerfair

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import android.widget.Space
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import dev.danielkeyes.kidscareerfair.theme.KidsCareerFairTheme
import dev.danielkeyes.kidscareerfair.theme.RainbowColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit
import androidx.camera.core.Preview as AndroidxCameraPreview

enum class Mode {
    BORING, RAINBOW, CONFETTI
}

class PhotoFrameFragment : Fragment() {
    // launcher for requesting permission
    private val launcher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { result ->
        if (result) {
            // TODO implement
        } else {
            // TODO implement
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // request camera permission
        launcher.launch(android.Manifest.permission.CAMERA)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)

        return ComposeView(requireContext()).apply {
            setContent {
                val lifecycleOwner = LocalLifecycleOwner.current
                val cameraProviderFuture = ProcessCameraProvider.getInstance(LocalContext.current)

                KidsCareerFairTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        // Why pass in the CameraPreview, because I love compose previews and
                        // camera2 has issues rendering. This allows me to see the preview still.
                        PhotoFrame(Modifier.fillMaxSize(),
                            content = {
                                CameraPreview(
                                    modifier = it
                                        .clipToBounds(),
                                    lifecycleOwner = lifecycleOwner,
                                    cameraProviderFuture = cameraProviderFuture,
                                    getRunnableAction = { a: ListenableFuture<ProcessCameraProvider>, b: PreviewView, c: LifecycleOwner ->
                                        getRunnableAction(a, b, c)
                                    }
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PhotoFrame(
    modifier: Modifier = Modifier,
    content: @Composable() (modifier: Modifier) -> Unit
) {
    val mode = rememberSaveable { mutableStateOf(Mode.BORING) }
    //------------ Rainbow --------------------
    val rainbowdelay = 900

    var currentIndex by remember { mutableIntStateOf(0) }

    val animatedColor = animateColorAsState(
        targetValue = RainbowColors[currentIndex],
        animationSpec = tween(durationMillis = rainbowdelay),
        label = ""
    ).value

    LaunchedEffect(key1 = Unit) {
        launch {
            while (true) {
                currentIndex = (currentIndex + 1) % RainbowColors.size
                delay(rainbowdelay.toLong())
            }
        }
    }

    val backgroundModifier = if (mode.value == Mode.RAINBOW) {
        modifier.background(animatedColor)
    } else {
        modifier
    }

    Column(
        modifier = backgroundModifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BorderText(
            text = "I can work with computers!",
        )
        LandScapeRowPortraitColumn(
            modifier = Modifier
                .padding(48.dp)
                .weight(1f)
        ) {
            content(
                Modifier
                    .weight(3f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Red)
                    .border(32.dp, color = MaterialTheme.colorScheme.primary)
            )

            LandScapeColumnPortraitRow(modifier = Modifier
                .weight(1f)
                .padding(16.dp)) {
                CustomToggle(title = "Boring", isChecked = mode.value == Mode.BORING) {
                    if (it) {
                        mode.value = Mode.BORING
                    }
                }
                CustomToggle(title = "Rainbow", isChecked = mode.value == Mode.RAINBOW) {
                    if (it) {
                        mode.value = Mode.RAINBOW
                    }
                }
                CustomToggle(title = "Confetti", isChecked = mode.value == Mode.CONFETTI) {
                    if (it) {
                        mode.value = Mode.CONFETTI
                    }
                }
            }
        }
        BorderText(text = "Leon Valley Elementary")
    }

    // Goes as the end to cover screen
    if (mode.value == Mode.CONFETTI) {
        RepeatingKonfetti()
    }
}

@Composable
fun BorderText(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp),
        text = text,
        style = TextStyle(fontSize = 36.sp),
        textAlign = TextAlign.Center,
    )
}

@Composable
fun LandScapeColumnPortraitRow(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        Column(modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly) {
            content()
        }
    } else {
        Row(modifier = modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly){
            content()
        }
    }
}

@Composable
fun LandScapeRowPortraitColumn(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (!isLandscape) {
        Column(modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally) {
            content()
        }
    } else {
        Row(modifier = modifier,
            verticalAlignment = Alignment.CenterVertically){
            content()
        }
    }
}

/**
 * Must be top(on top) composable.
 * Fills max size to overlay container.
 * This will remove old Konfetti
 * @param timeMillis how long before displaying new confetti
 */
@Composable
fun RepeatingKonfetti(timeMillis: Long = 1000) {
    var flip by remember { mutableStateOf(true) }
    val emitter = Emitter(duration = 100L, timeUnit = TimeUnit.MILLISECONDS).max(100)
    var party by remember { mutableStateOf(Party(emitter = emitter)) }

    // Sets Konfetti party to new part of screen and "flips" flip switch
    // Changing flip switch forces recomposition for continued Konfetti
    LaunchedEffect(Unit) {
        while (true) {
            delay(timeMillis)
            party = Party(
                position = Position.Relative(
                    Math.random() * 0.6 + 0.2, // Avoids going to outer edges
                    Math.random() * 0.6 + 0.2
                ),
                emitter = Emitter(duration = 100L, timeUnit = TimeUnit.MILLISECONDS).max(100)
            )
            flip = !flip
        }
    }

    when (flip) {
        true ->
            KonfettiView(modifier = Modifier.fillMaxSize(), parties = listOf(party))
        false ->
            KonfettiView(modifier = Modifier.fillMaxSize(), parties = listOf(party))
    }
}

@Composable
fun CameraPreview(
    modifier: Modifier,
    lifecycleOwner: LifecycleOwner,
    getRunnableAction: (ListenableFuture<ProcessCameraProvider>, PreviewView, LifecycleOwner) -> Runnable,
    cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
) {
    Box(
        modifier = modifier
            .clipToBounds()
    ) {
        AndroidView(
            modifier = modifier.align(Alignment.Center),
            factory = { context ->
                PreviewView(context).apply {
                    setBackgroundColor(0xff00ff00.toInt())
                    layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    post {
                        cameraProviderFuture.addListener(
                            getRunnableAction(
                                cameraProviderFuture,
                                this,
                                lifecycleOwner
                            ), ContextCompat.getMainExecutor(context)
                        )
                    }
                }
            }
        )
    }
}

fun getRunnableAction(
    cameraProviderFuture: ListenableFuture<ProcessCameraProvider>,
    previewView: PreviewView,
    lifecycleOwner: LifecycleOwner,
): Runnable {
    return Runnable {
        val cameraProvider = cameraProviderFuture.get()
        bindPreview(
            cameraProvider,
            lifecycleOwner,
            previewView,
        )
    }
}

fun bindPreview(
    cameraProvider: ProcessCameraProvider,
    lifecycleOwner: LifecycleOwner,
    previewView: PreviewView,
) {
    val preview: AndroidxCameraPreview = AndroidxCameraPreview.Builder()
        .build()

    val cameraSelector: CameraSelector = CameraSelector.Builder()
        .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
        .build()

    preview.setSurfaceProvider(previewView.surfaceProvider)

    cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview)
}

@Composable
fun CustomToggle(
    modifier: Modifier = Modifier,
    title: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Column(
        modifier = modifier.padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val textModifier = if(isChecked) {
            Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primary)
        } else {
            Modifier
        }

        Text(
            modifier = textModifier.padding(8.dp),
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(16.dp))
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Preview
@Composable
private fun PreviewCustomToggle() {
    KidsCareerFairTheme {
        Surface {
            Row {
                CustomToggle(
                    title = "Title",
                    isChecked = true,
                    onCheckedChange = {}
                )
                Spacer(modifier = Modifier.width(16.dp))
                CustomToggle(
                    title = "Title",
                    isChecked = false,
                    onCheckedChange = {}
                )
            }

        }
    }
}

@Preview(showBackground = true, name = "10-inch Tablet Portrait", widthDp = 600, heightDp = 960)
@Preview(showBackground = true, name = "10-inch Tablet Landscape", widthDp = 960, heightDp = 600)
@Preview(showBackground = true, name = "pixel4", device = "id:pixel_4")
@Preview(showBackground = true, name = "automotive", device = Devices. AUTOMOTIVE_1024p)
@Composable
private fun PreviewPhotoFrame() {
    KidsCareerFairTheme {
        Surface {
            PhotoFrame(
                content = {
                    Box(
                        modifier = it
                            .background(Color.Red)
                            .fillMaxSize()
                    )
                }
            )
        }
    }
}