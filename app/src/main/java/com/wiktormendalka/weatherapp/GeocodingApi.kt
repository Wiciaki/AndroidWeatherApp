import retrofit2.http.GET
import retrofit2.http.Query

data class GeocodingResult(
    val id: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val elevation: Int,
    val feature_code: String,
    val country_code: String,
    val admin1_id: Int,
    val admin2_id: Int,
    val admin3_id: Int,
    val timezone: String,
    val population: Int,
    val country_id: Int,
    val country: String,
    val admin1: String,
    val admin2: String,
    val admin3: String
)

data class GeocodingResponse(
    val results: List<GeocodingResult>,
    val generationtime_ms: Double
)

interface GeocodingApi {
    @GET("search")
    suspend fun getLocationForName(
        @Query("name") name: String,
        @Query("count") count: Int,
        @Query("language") language: String,
        @Query("format") format: String
    ): GeocodingResponse
}