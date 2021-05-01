package ipvc.estg.commov.api



import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface Endpoints {



    @GET("users/{email}")
    fun getUserByEmail(@Path("email") email: String): Call<User>

    @GET("reports")
    fun getReports(): Call<List<Report>>

    @GET("tipo")
    fun getTipos(): Call<List<Tipo>>

    @Multipart
    @POST("reports")
    fun newReport(
            @Part("titulo") titulo:RequestBody,
            @Part("descricao") descricao: RequestBody,
            @Part("latitude") latitude: RequestBody,
            @Part("longitude") longitude: RequestBody,
            @Part("user_id") user_id: RequestBody,
            @Part("tipo") tipo: RequestBody,
            @Part imagem: MultipartBody.Part
    ): Call<PostOutput>


}