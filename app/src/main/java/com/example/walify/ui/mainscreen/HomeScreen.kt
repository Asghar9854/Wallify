package com.example.walify.ui.mainscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.walify.R
import com.example.walify.model.Photo
import com.example.walify.utils.isNetworkAvailable
import com.example.walify.utils.pixelApiKey
import com.example.walify.viewmodel.MainViewModel
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets




@Composable
fun MainScreen(navController: NavHostController, viewModel: MainViewModel) {
    Column {
        TopBar(navController)
        FetchApiData(viewModel, navController)
    }
}

@Composable
fun FetchApiData(
    viewModel: MainViewModel,
    navController: NavController
) {
    val apiKey = pixelApiKey
    val query = "nature"
    val perPage = 16
    val context = LocalContext.current
    if (context.isNetworkAvailable()) {
        // Call API
        LaunchedEffect(Unit) {
            viewModel.wallpapers(apiKey, query, perPage)
        }

        // Observe LiveData using observeAsState
        val response by viewModel.apiResponse.observeAsState()

        // If the response is not null, show wallpapers
        response?.let { it ->
            SetDataOnRecyclerview(
                photoList = it.photos,
                navController = navController
            ) // Pass the necessary data to your WallpaperCard composable
        }
    } else {
        // Handle no network case
        Text("No network available")
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController) {
    TopAppBar(
        title = { Text(text = "Wallify") },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.White),
        actions = {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "search"
                )
            }
            IconButton(onClick = {
                navController.navigate("savedImages")
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_saved),
                    contentDescription = "saved"
                )
            }

        }
    )
}

@Composable
fun SetDataOnRecyclerview(photoList: List<Photo>, navController: NavController) {

    val imageList = remember {
        photoList
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(8.dp), // External padding for the grid
        contentPadding = PaddingValues(8.dp), // External padding for content
        verticalArrangement = Arrangement.spacedBy(16.dp), // Vertical space between rows
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(imageList) {
            SingleItemMain(it, navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleItemMain(photo: Photo, navController: NavController) {
    // Convert the Photo object to a JSON string
    val photoJson = Gson().toJson(photo)
    // URL encode the JSON string to handle special characters
    val encodedPhotoJson = URLEncoder.encode(photoJson, StandardCharsets.UTF_8.toString())

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(10.dp)),
        onClick = {
            navController.navigate("fullScreen/$encodedPhotoJson")
        }
    ) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(photo.src.portrait)
                        .crossfade(true)
                        .build()
                ),
                contentDescription = "Wallpapers",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
        }
    }
}
