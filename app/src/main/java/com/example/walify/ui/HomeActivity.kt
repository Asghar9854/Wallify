package com.example.walify.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.walify.R
import com.example.walify.dataBase.savePhoto
import com.example.walify.model.Photo
import com.example.walify.ui.editImage.EditImageScreen
import com.example.walify.ui.mainscreen.MainScreen
import com.example.walify.ui.mainscreen.SetDataOnRecyclerview
import com.example.walify.ui.saved.SavedImagesScreen
import com.example.walify.ui.viewImage.ViewWallpaperScreen
import com.example.walify.utils.isNetworkAvailable
import com.example.walify.utils.pixelApiKey
import com.example.walify.viewmodel.MainViewModel
import com.example.walify.viewmodel.SavedViewModel
import com.google.gson.Gson
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

class HomeActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels<MainViewModel>()
    private val savedViewModel: SavedViewModel by viewModels<SavedViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            AppHost(
                navController,
                viewModel,
                savedViewModel
            )
        }
    }
}

@Composable
fun AppHost(
    navController: NavHostController,
    viewModel: MainViewModel,
    savedViewModel: SavedViewModel
) {
    NavHost(navController = navController, startDestination = "grid")
    {
        composable("grid") {
            MainScreen(navController, viewModel)
        }
        composable(
            route = "fullScreen/{photo}",
            arguments = listOf(navArgument("photo") { type = NavType.StringType })
        ) { backStackEntry ->
            val encodedJsonPhoto = backStackEntry.arguments?.getString("photo")
            encodedJsonPhoto?.let {
                val decodedJsonPhoto = URLDecoder.decode(
                    it,
                    StandardCharsets.UTF_8.toString()
                ) // Decode it
                val photo = Gson().fromJson(
                    decodedJsonPhoto,
                    Photo::class.java
                ) // Deserialize back to Photo
                ViewWallpaperScreen(photo = photo, navController = navController)
            }
        }

        composable("savedImages") {
            SavedImagesScreen(
                savedViewModel = savedViewModel,
                navController
            )
        }

        composable(
            route = "editScreen/{photo}",
            arguments = listOf(navArgument("photo") { type = NavType.StringType })
        ) { backStackEntry ->
            val encodedJsonPhoto = backStackEntry.arguments?.getString("photo")
            encodedJsonPhoto?.let {
                val decodedJsonPhoto = URLDecoder.decode(
                    it,
                    StandardCharsets.UTF_8.toString()
                ) // Decode it
                val photo = Gson().fromJson(
                    decodedJsonPhoto,
                    savePhoto::class.java
                ) // Deserialize back to Photo
                EditImageScreen(photo = photo, navController = navController)
            }
        }

    }
}

