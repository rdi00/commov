package ipvc.estg.commov.api

import java.sql.Date
import java.text.DateFormat


data class Report(
    val id: Int,
    val titulo: String,
    val descricao: String,
    val latitude: Float,
    val longitude: Float,
    val user_id: Int,
    val estado: String,
    val data_criacao: String,
    val tipo: String,
    val imagem: String

)