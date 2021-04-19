package ipvc.estg.commov.api



import retrofit2.Call
import retrofit2.http.*


interface Endpoints {

    @GET("/users/")
    fun getUsers(): Call<List<User>>

    @GET("/users/{email}")
    fun getUserByEmail(@Path("email") email: String): Call<User>


}