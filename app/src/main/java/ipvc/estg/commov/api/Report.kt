package ipvc.estg.commov.api



data class Report(
    val id: Int,
    val titulo: String,
    val descricao: String,
    val latitude: String,
    val longitude: String,
    val user_id: Int,
    val estado: String,
    val data_criacao: String,
    val tipo: String,
    val imagem: String

)