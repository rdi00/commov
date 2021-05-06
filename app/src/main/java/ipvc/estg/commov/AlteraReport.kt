package ipvc.estg.commov

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import ipvc.estg.commov.api.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class AlteraReport : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_elimina_report)

        var intent = intent
        val tit = intent.getStringExtra("tit")
        val snip = intent.getStringExtra("snip")

        val campos = snip.split("_")
        //0-descriçao
        //1-report userid
        //2-id user sharedp
        //3-imagem
        //4-id report
        //5- tipo
        //6-data

        val imageBytes = Base64.getDecoder().decode(campos[3])
        val decoded = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        findViewById<EditText>(R.id.editTituloR).setText(tit)
        findViewById<EditText>(R.id.DescEditR).setText(campos[0])
        findViewById<TextView>(R.id.tipoEdit).setText(campos[5])
        findViewById<TextView>(R.id.dataEdit).setText(campos[6])
        findViewById<ImageView>(R.id.imageView2).setImageBitmap(decoded)
        val edit=findViewById<Button>(R.id.editbtn)
        val delete=findViewById<Button>(R.id.elimR)

        if(campos[1].equals(campos[2])){
            edit.visibility = View.VISIBLE
            delete.visibility = View.VISIBLE
        }else{
            edit.visibility = (View.INVISIBLE)
            delete.visibility = (View.INVISIBLE)
        }

        val id = campos[4].toInt()

    }

    fun editar(view: View){
        var intent = intent
        val tit = intent.getStringExtra("tit")
        val snip = intent.getStringExtra("snip")

        val campos = snip.split("_")
        //0-descriçao
        //1-report userid
        //2-id user sharedp
        //3-imagem
        //4-id report
        //5- tipo
        //6-data

        val id = campos[4].toInt()
        Log.d("AAAAAAAAAAAA", id.toString())

        val editadoT = findViewById<EditText>(R.id.editTituloR).text.toString()
        val editadoD = findViewById<EditText>(R.id.DescEditR).text.toString()

        val request = ServiceBuilder.buildService(Endpoints::class.java)
        val call = request.editReports(id,editadoT,editadoD)
        var intento = Intent(this, MapReports::class.java)

        call.enqueue(object : Callback<ReportResponse> {
            override fun onResponse(call: Call<ReportResponse>, response: Response<ReportResponse>) {
                if (response.isSuccessful){
                    Toast.makeText(this@AlteraReport, getString(R.string.atualizaReport), Toast.LENGTH_SHORT).show()
                    startActivity(intento)

                }else {
                    Toast.makeText(this@AlteraReport, getString(R.string.erroAtualizaReport), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ReportResponse>, t: Throwable) {
                Toast.makeText(this@AlteraReport, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}