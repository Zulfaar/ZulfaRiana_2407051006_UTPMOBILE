package com.example.percobaan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.example.percobaan.model.Video
import com.example.percobaan.model.VideoSource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            Scaffold(
                bottomBar = { BottomBar() }
            ) { paddingValues ->

                NavHost(
                    navController = navController,
                    startDestination = "home",
                    modifier = Modifier.padding(paddingValues)
                ) {

                    composable("home") {
                        HomeScreen(navController)
                    }

                    composable("detail/{title}/{channel}/{views}/{image}") { backStack ->
                        DetailScreen(
                            title = backStack.arguments?.getString("title") ?: "",
                            channel = backStack.arguments?.getString("channel") ?: "",
                            views = backStack.arguments?.getString("views") ?: "",
                            image = backStack.arguments?.getString("image")?.toInt() ?: 0
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(navController: NavController) {

    LazyColumn(modifier = Modifier.padding(16.dp)) {

        item {
            ProfileSection()
            Spacer(modifier = Modifier.height(16.dp))
        }

        items(VideoSource.videos) { video ->
            VideoItem(video) {
                navController.navigate(
                    "detail/${video.title}/${video.channelName}/${video.views}/${video.imageRes}"
                )
            }
        }
    }
}

@Composable
fun ProfileSection() {
    Row(verticalAlignment = Alignment.CenterVertically) {

        Image(
            painter = painterResource(id = R.drawable.profile),
            contentDescription = "",
            modifier = Modifier.size(50.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Column {
            Text("Zulfa Riana", style = MaterialTheme.typography.titleMedium)
            Text("@zulfacantik", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun VideoItem(video: Video, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable { onClick() }
    ) {

        Image(
            painter = painterResource(id = video.imageRes),
            contentDescription = "",
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Column {
            Text(video.title)
            Text(video.channelName)
            Text(video.views)
        }
    }
}

@Composable
fun DetailScreen(title: String, channel: String, views: String, image: Int) {

    var liked by remember { mutableStateOf(false) }
    var subscribed by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {

        Image(
            painter = painterResource(id = image),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(title, style = MaterialTheme.typography.titleMedium)
        Text(views)

        Spacer(modifier = Modifier.height(10.dp))

        Row {
            Button(onClick = { liked = !liked }) {
                Text(if (liked) "Liked ❤️" else "Like 👍")
            }

            Spacer(modifier = Modifier.width(10.dp))

            Button(onClick = { subscribed = !subscribed }) {
                Text(if (subscribed) "Subscribed 🔔" else "Subscribe")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(channel)
    }
}

@Composable
fun BottomBar() {
    NavigationBar {

        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "") },
            label = { Text("Beranda") },
            selected = true,
            onClick = {}
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.List, contentDescription = "") },
            label = { Text("Koleksi") },
            selected = false,
            onClick = {}
        )
    }
}