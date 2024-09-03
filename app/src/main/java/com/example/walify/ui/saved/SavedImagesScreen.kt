package com.example.walify.ui.saved

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.walify.dataBase.savePhoto
import com.example.walify.utils.showToast
import com.example.walify.viewmodel.SavedViewModel
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedImagesScreen(savedViewModel: SavedViewModel, navController: NavController) {
    val context = LocalContext.current
    val savedPhotos by savedViewModel.getSavedPhotos(context).observeAsState(emptyList())
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Saved Images") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.White)
            )
        },
        content = { padding ->
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(savedPhotos) { savedPhoto ->
                    SavedPhotoCard(navController,savedPhoto)
                }
            }
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedPhotoCard(navController: NavController,savedPhoto: savePhoto) {
    // Convert the Photo object to a JSON string
    val photoJson = Gson().toJson(savedPhoto)
    // URL encode the JSON string to handle special characters
    val encodedPhotoJson = URLEncoder.encode(photoJson, StandardCharsets.UTF_8.toString())
    val context = LocalContext.current
    val painter =
        rememberImagePainter(data = savedPhoto.imagePath) // Make sure to provide a valid image path

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        onClick = {
            navController.navigate("editScreen/$encodedPhotoJson")
        }
    ) {
        Column {
            Image(
                painter = painter,
                contentDescription = "Saved Photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Text(
                text = savedPhoto.name,
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}