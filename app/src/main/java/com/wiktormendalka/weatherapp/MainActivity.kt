package com.wiktormendalka.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wiktormendalka.weatherapp.ui.theme.WeatherAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigator()
        }
    }
}

enum class Screen {
    SelectorView,
    DetailsView
}

@Composable
fun AppNavigator() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Screen.SelectorView.name) {
        composable(Screen.SelectorView.name) { SelectorView(navController) }
        composable(Screen.DetailsView.name) { DetailsView(navController) }
    }
}

@Composable
fun SelectorView(navController: NavController) {
    var selectedCity by rememberSaveable { mutableStateOf("Krakow") }

    WeatherAppTheme {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Column(Modifier.align(Alignment.Center),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Image(painter = painterResource(id = R.drawable.weather), "", modifier = Modifier.fillMaxWidth(0.3f), contentScale = ContentScale.Fit)
                Text(text = "Podaj swoją lokalizację", fontSize = 25.sp)
                TextField(
                    value = selectedCity,
                    onValueChange = { selectedCity = it },
                    label = { Text("Nazwa miasta (j. angielski)") }
                )
                OutlinedButton(onClick = {
                    navController.currentBackStackEntry?.savedStateHandle?.set("city", selectedCity)
                    navController.navigate(Screen.DetailsView.name)
                }) {
                    Text(text = "Szukaj")
                }
            }
        }
    }
}

@Composable
fun DetailsView(navController: NavController) {
    val selectedCity = navController.previousBackStackEntry?.savedStateHandle?.get<String>("city") ?: throw Exception()

    val viewModel: WeatherViewModel = viewModel()
    viewModel.fetchLocation(selectedCity)

    val locationData = viewModel.locationData.value
    val weatherData = viewModel.weatherData.value

    WeatherAppTheme {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Column(Modifier.align(Alignment.Center),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {

                Text(text = "Miasto", fontSize = 25.sp)
                Text(text = locationData?.admin2 + ", " + locationData?.admin1)
                Text(text = locationData?.admin3.toString())

                Spacer(modifier = Modifier.absoluteOffset(20.dp))

                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Absolute.SpaceEvenly) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Ludność", fontSize = 20.sp)
                        Text(text = locationData?.population.toString())
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Strefa czasowa", fontSize = 20.sp)
                        Text(text = weatherData?.timezone.toString())
                    }
                }

                Spacer(modifier = Modifier.absoluteOffset(20.dp))

                Text(text = "Temperatura", fontSize = 25.sp)
                Text(text = weatherData?.current?.temperature_2m.toString() + "°C, odczuwalna " + weatherData?.current?.apparent_temperature + "°C")

                Text(text = "Deszcz", fontSize = 25.sp)
                Text(text = weatherData?.current?.rain.toString() + weatherData?.current_units?.rain)
            }
        }
    }
}

