package ipvc.estg.commov.api


import retrofit2.Call
import retrofit2.http.*


interface Endpoints {
    @GET("users/{email}")
    fun getUserByEmail(@Path("email") email: String): Call<User>

    @GET("reports")
    fun getReports(): Call<List<Report>>

    @FormUrlEncoded
    @POST("report")
    fun newReport(@Field("titulo")titulo:String,
            @Field("descricao")descricao:String,
            @Field("latitude")latitude:String,
            @Field("longitude")longitude:String,
            @Field("user_id")user_id:Int,
            @Field("tipo")tipo:String,
            @Field("imagem")imagem:String) : Call<ReportAdd>

}