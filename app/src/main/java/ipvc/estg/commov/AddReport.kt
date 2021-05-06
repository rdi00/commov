package ipvc.estg.commov

import android.util.Log
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.gms.maps.model.LatLng
import ipvc.estg.commov.api.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.util.*
import com.google.android.gms.location.*


class AddReport : AppCompatActivity() {

    private lateinit var image: ImageView
    val REQUEST_CODE = 1
    private lateinit var titulo: EditText
    private lateinit var descricao: EditText
    private var latitude:  Double = 0.0
    private var longitude: Double = 0.0
    private var tipo: Any? = null
    private var user_id: Int = 0
    private lateinit var imagem: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_report)

        image= findViewById<ImageView>(R.id.imgadd)
        titulo = findViewById<EditText>(R.id.editTitulo)
        descricao = findViewById<EditText>(R.id.editdesc)
        latitude = intent.getParcelableExtra<LatLng>("local")!!.latitude
        longitude = intent.getParcelableExtra<LatLng>("local")!!.longitude
        user_id = intent.getStringExtra("user_id").toInt()





        //spinner
        val spnTipos =findViewById<Spinner>(R.id.tipos)
        val listTipos : Array<String> = arrayOf(getString(R.string.transito), getString(R.string.obras), getString(R.string.buraco) , getString(R.string.acidente) , getString(R.string.outros))
        val spnAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listTipos.toList())
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnTipos.adapter = spnAdapter
        tipo = spnTipos.selectedItem


        val add= findViewById<Button>(R.id.addreport)

        add.setOnClickListener {


            Log.d("UUUUUUUUUUUUUUUUUUUSEID",user_id.toString())

                //if(tituloField.text.isNullOrEmpty()){
                  //  Toast.makeText(this@AddReport, "Titulo nao pode ser vazio", Toast.LENGTH_SHORT)
                //}
                //if (descricaoField.text.isNullOrEmpty()) {
                    //Toast.makeText(this@AddReport, "Descricao nao pode ser vazio", Toast.LENGTH_SHORT)
                //}

                    ///else {

                Log.d("AAAAAAAAAAAAAA", titulo.text.toString())

                    val request = ServiceBuilder.buildService(Endpoints::class.java)
                    val call = request.newReport(titulo = titulo.text.toString(),descricao = descricao.text.toString(),latitude = latitude.toString(),longitude = longitude.toString(),user_id = user_id,tipo =  tipo.toString(),imagem = imagem.toString())

            call.enqueue(object : Callback<ReportResponse> {
                            override fun onResponse(call: Call<ReportResponse>, response: Response<ReportResponse>) {
                                if (response.isSuccessful) {
                                    Log.d("*******OLA*****", response.body().toString())
                                    Toast.makeText(this@AddReport, getString(R.string.rport_add_sucesso), Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this@AddReport,MapReports::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    Log.d("WWWWWWWWWWWWWWW", response.body().toString())
                                    Toast.makeText(this@AddReport, getString(R.string.erro_add_report), Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<ReportResponse>, t:Throwable) {

                                Toast.makeText(this@AddReport, "${t.message}", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }

                //}
           // else {
               // Toast.makeText(this@AddReport, "Tem de selecionar uma imagem", Toast.LENGTH_SHORT).show()
           // }

        //}

    }

    //escolher imagem
    fun getImage(view: View) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    //definir path local da imagem
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
                image.setImageURI(data?.data)
            val img = findViewById<ImageView>(R.id.imgadd).drawable
            val bos = ByteArrayOutputStream()
            val imgBit: Bitmap = (img as BitmapDrawable).bitmap
            imgBit.compress(Bitmap.CompressFormat.JPEG, 100, bos)
             imagem = Base64.getEncoder().encodeToString(bos.toByteArray())
            Log.d("ENDODEDIMAGE", imagem)

        }
    }


}