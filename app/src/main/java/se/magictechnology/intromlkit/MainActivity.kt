package se.magictechnology.intromlkit

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collect
import se.magictechnology.intromlkit.ui.MainViewModel
import se.magictechnology.intromlkit.ui.theme.IntroMLKitTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IntroMLKitTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        resources = resources
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel(),
    resources: Resources
) {
    val receiptDrawable = R.drawable.receipt
    val streetSignDrawable = R.drawable.street_sign
    val trainScheduleDrawable = R.drawable.train_schedule
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val screenState = viewModel.sharedFlow.collectAsState(initial = "")
        println(screenState.value)
        Text(modifier = Modifier
            .padding(16.dp),
            text = screenState.value ?: ""
        )
        TextImageWithButton(receiptDrawable) {
            val selectedImage = BitmapFactory.decodeResource(resources, receiptDrawable)
            viewModel.runTextRecognition(selectedImage)
        }
        TextImageWithButton(streetSignDrawable) {
            val selectedImage = BitmapFactory.decodeResource(resources, streetSignDrawable)
            viewModel.runTextRecognition(selectedImage)
        }
        TextImageWithButton(trainScheduleDrawable) {
            val selectedImage = BitmapFactory.decodeResource(resources, trainScheduleDrawable)
            viewModel.runTextRecognition(selectedImage)
        }
    }
}

@Composable
fun TextImageWithButton(
    @DrawableRes imageIdRes: Int,
    processImage: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextImage(imageIdRes)
        Button(onClick = {
            processImage()
        }) {
            Text("Process image")
        }
    }
}

@Composable
fun TextImage(@DrawableRes imageIdRes: Int) {
    Card(
        modifier = Modifier.size(240.dp),
    ) {
        Image(
            painterResource(imageIdRes),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}