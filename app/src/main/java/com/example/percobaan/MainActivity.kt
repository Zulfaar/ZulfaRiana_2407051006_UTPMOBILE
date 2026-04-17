package com.example.percobaan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.example.percobaan.model.VideoSource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            var tab by remember { mutableStateOf(0) }
            val navController = rememberNavController()

            Scaffold(
                bottomBar = {
                    NavigationBar {
                        NavigationBarItem(
                            selected = tab == 0,
                            onClick = { tab = 0 },
                            icon = { Icon(Icons.Default.Home, null) },
                            label = { Text("Beranda") }
                        )
                        NavigationBarItem(
                            selected = tab == 1,
                            onClick = { tab = 1 },
                            icon = { Icon(Icons.Default.List, null) },
                            label = { Text("Koleksi") }
                        )
                    }
                }
            ) { padding ->

                if (tab == 0) {
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.padding(padding)
                    ) {

                        composable("home") {
                            HomeScreen(navController)
                        }

                        composable("detail/{title}/{channel}/{views}/{image}") { it ->
                            DetailScreen(
                                title = it.arguments?.getString("title") ?: "",
                                channel = it.arguments?.getString("channel") ?: "",
                                views = it.arguments?.getString("views") ?: "",
                                image = it.arguments?.getString("image")?.toInt() ?: 0,
                                navController = navController
                            )
                        }
                    }
                } else {
                    CollectionScreen()
                }
            }
        }
    }
}

@Composable
fun HomeScreen(navController: NavController) {

    LazyColumn {

        item {
            Text(
                "YouTAM",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )
        }

        items(VideoSource.videos) { video ->

            Column(
                modifier = Modifier.clickable {
                    navController.navigate(
                        "detail/${video.title}/${video.channelName}/${video.views}/${video.imageRes}"
                    )
                }
            ) {

                Image(
                    painter = painterResource(video.imageRes),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )

                Column(modifier = Modifier.padding(10.dp)) {
                    Text(video.title)
                    Text("${video.channelName} • ${video.views}")
                }
            }
        }
    }
}

@Composable
fun DetailScreen(
    title: String,
    channel: String,
    views: String,
    image: Int,
    navController: NavController
) {

    var liked by remember { mutableStateOf(false) }
    var subscribed by remember { mutableStateOf(false) }

    Column {

        Box {
            Image(
                painter = painterResource(image),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            )

            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "",
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { navController.popBackStack() }
            )
        }

        Column(modifier = Modifier.padding(16.dp)) {

            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(views)

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { liked = !liked }
                ) {
                    Icon(Icons.Default.ThumbUp, "")
                    Text(if (liked) "Liked ❤️" else "Suka")
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Share, "")
                    Text("Bagikan")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                Image(
                    painter = painterResource(R.drawable.profile),
                    contentDescription = "",
                    modifier = Modifier.size(40.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(channel)
                    Text("1.5M Subscriber")
                }

                Button(onClick = { subscribed = !subscribed }) {
                    Text(if (subscribed) "Subscribed 🔔" else "Subscribe")
                }
            }
        }
    }
}

@Composable
fun CollectionScreen() {

    LazyColumn(modifier = Modifier.padding(16.dp)) {

        item {
            Row(verticalAlignment = Alignment.CenterVertically) {

                Image(
                    painter = painterResource(R.drawable.profile),
                    contentDescription = "",
                    modifier = Modifier.size(50.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text("Zulfa Riana")
                    Text("@zulfacantik", color = androidx.compose.ui.graphics.Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Riwayat")
        }

        item {
            Row {

                Image(
                    painter = painterResource(R.drawable.mamank),
                    contentDescription = "",
                    modifier = Modifier.size(120.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Image(
                    painter = painterResource(R.drawable.wasa),
                    contentDescription = "",
                    modifier = Modifier.size(120.dp)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Daftar Putar (Playlist)")
        }

        items(5) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Icon(Icons.Default.List, "")
                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text("Playlist Kuliah ${it + 1}")
                    Text("${(it + 1) * 3} video")
                }
            }
        }
    }
}