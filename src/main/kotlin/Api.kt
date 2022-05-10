import com.google.gson.GsonBuilder
import db.model.User
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface Api {

    @GET("/get_users")
    fun getUsers(
    ): Call<UserListResponse>

    @POST("/delete_user")
    fun deleteUser(
        @Body body: DeleteBody
    ): Call<String>


    @POST("/add_user")
    fun addUser(
        @Body body: AddUserBody
    ): Call<String>

    @POST("/update_user")
    fun updateUser(
        @Body body: UpdateUserBody
    ): Call<String>



    data class StringResponse(
        val response: String
    )

    data class UserListResponse(
        val response: List<User>
    )

    data class DeleteBody(
        val id: Int
    )

    data class AddUserBody(
        var name: String,
        var surname: String,
        var country: String
    )

    data class UpdateUserBody(
        var id: Int,
        var name: String,
        var surname: String,
        var country: String
    )

    data class ImgResponse(
        val response: ByteArray
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ImgResponse

            if (!response.contentEquals(other.response)) return false

            return true
        }

        override fun hashCode(): Int {
            return response.contentHashCode()
        }
    }

    companion object {
        private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .build()


        private val gson = GsonBuilder()
            .setLenient()
            .create()

        fun invoke(): Api {

            return Retrofit.Builder()
                .baseUrl("http://localhost:2121")
                .addConverterFactory(GsonConverterFactory.create(gson))

                .client(okHttpClient)
                .build()
                .create(Api::class.java)
        }
    }
}