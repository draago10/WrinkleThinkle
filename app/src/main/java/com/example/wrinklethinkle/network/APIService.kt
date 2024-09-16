import retrofit2.Call
import retrofit2.GET
import retrofit2.http.Query
import com.example.wrinklethinkle.model.PlantResponse

// This interface defines the API endpoints for interacting with the Trefle API.
interface APIService {

    // This function sends a GET request to the /plants/search endpoint with a query parameter
    // for the search term (e.g., "coconut") and the API token.
    @GET("plants/search")
    fun searchPlants(
        @Query("token") token: String,    // Query parameter for the API token
        @Query("q") query: String         // Query parameter for the search term (e.g., plant name)
    ): Call<PlantResponse>                // Retrofit will return a Call object with PlantResponse
}
