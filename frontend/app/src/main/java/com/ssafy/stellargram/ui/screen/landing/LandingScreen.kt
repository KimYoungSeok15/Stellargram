package com.ssafy.stellargram.ui.screen.landing

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import com.ssafy.stellargram.R
import com.ssafy.stellargram.ui.Screen
import com.ssafy.stellargram.ui.common.CustomTextButton
import java.io.File

@Composable
@androidx.media3.common.util.UnstableApi
fun LandingScreen(navController: NavController) {
    Column(
        modifier = Modifier
    ) {
        Box(contentAlignment = Alignment.Center) {
            MediaDevice(Modifier.zIndex(0f))
            LandingComponent(navController, Modifier.zIndex(1f))
        }
    }
}

@Composable
fun LandingComponent(navController: NavController, modifier: Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier
    ) {
        Image(
            painter =
            painterResource(id = R.drawable.stellargram_1_),
            contentDescription = "LOGO",
            modifier = Modifier.size(200.dp)
        )
        Image(
            painter =
            painterResource(id = R.drawable.kakao_login_large_narrow),
            contentDescription = "LOGIN",
            modifier = Modifier
                .clickable { navController.navigate("kakao") }
        )


         // TODO: 채팅방 임시 연결버튼. 나중에 지우기
//         Button(
//             onClick = {
// //                navController.navigate(Screen.ChatRoom.route)
//                 navController.navigate(route = Screen.ChatRoom.route + "/1/2/testsite")
//             },
//             modifier = Modifier
//                 .padding(16.dp)
//         ) {
//             Text(text = "채팅방")
//         }
    }
}


@Composable
@androidx.media3.common.util.UnstableApi
fun MediaDevice(modifier: Modifier) {
    val context = LocalContext.current
//    val url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
    val uri = Uri.parse(
        ContentResolver.SCHEME_ANDROID_RESOURCE
                + File.pathSeparator + File.separator + File.separator
                + context.packageName
                + File.separator
                + R.raw.landing_bg_1440
    )
//    val mediaItem = MediaItem.fromUri(Uri.parse(url))
    val mediaItem = MediaItem.fromUri(uri)
    var exoPlayer = remember {
        ExoPlayer.Builder(context).build()
            .apply {
                setMediaItem(mediaItem)
                playWhenReady = true
                prepare()
                volume = 0f
                repeatMode = Player.REPEAT_MODE_ONE
            }
    }
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
            Log.d("EXOPLAYER", "DISPOSED")
        }
    }
    Box(modifier = modifier) {
        AndroidView(factory = {
            PlayerView(it).apply {
                player = exoPlayer
                useController = false
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            }
        })
        Box(
            modifier = Modifier
                .matchParentSize()
                .alpha(0.5f)
                .background(Color.Black)
        )
    }
}
