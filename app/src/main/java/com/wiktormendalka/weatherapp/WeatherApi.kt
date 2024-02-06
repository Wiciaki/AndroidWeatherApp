import retrofit2.http.GET
import retrofit2.http.Query

data class CurrentUnits(
    val time: String,
    val interval: String,
    val temperature_2m: String,
    val relative_humidity_2m: String,
    val apparent_temperature: String,
    val is_day: String,
    val precipitation: String,
    val rain: String,
    val showers: String,
    val snowfall: String,
    val weather_code: String,
    val cloud_cover: String,
    val pressure_msl: String,
    val surface_pressure: String,
    val wind_speed_10m: String,
    val wind_direction_10m: String,
    val wind_gusts_10m: String
)

data class Current(
    val time: String,
    val interval: Int,
    val temperature_2m: Double,
    val relative_humidity_2m: Double,
    val apparent_temperature: Double,
    val is_day: Int,
    val precipitation: Double,
    val rain: Double,
    val showers: Double,
    val snowfall: Double,
    val weather_code: Int,
    val cloud_cover: Double,
    val pressure_msl: Double,
    val surface_pressure: Double,
    val wind_speed_10m: Double,
    val wind_direction_10m: Double,
    val wind_gusts_10m: Double
)

data class WeatherResponse(
    val latitude: Double,
    val longitude: Double,
    val generationtime_ms: Double,
    val utc_offset_seconds: Int,
    val timezone: String,
    val timezone_abbreviation: String,
    val elevation: Int,
    val current_units: CurrentUnits,
    val current: Current
)

interface WeatherApi {
    @GET("forecast")
    suspend fun getWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String,
        @Query("timezone") timezone: String,
        @Query("forecast_days") forecastDays: Int
    ): WeatherResponse
}