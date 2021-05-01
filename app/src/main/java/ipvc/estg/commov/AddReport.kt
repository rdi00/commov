

package ipvc.estg.commov

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.graphics.drawable.toBitmap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ipvc.estg.commov.api.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*

class AddReport : AppCompatActivity() {

    val REQUEST_CODE = 1
    val imgadd = findViewById<ImageView>(R.id.imgadd)

//escolher imagem
    fun getImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    //definir path local da imagem
    override fun onActivityResult(requestCode: Int, resultCode: Int, i: Intent?) {
        super.onActivityResult(requestCode, resultCode, i)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            imgadd.setImageURI(i?.data)
        }
    }

//converter a imagem
    private fun convertImg(fileName: String, bitmap: Bitmap): File {

    //cria novo ficheiro
    val ficheiro = File(this@AddReport.cacheDir, fileName)
    ficheiro.createNewFile()
    try{
    //bitmap para bytearray
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val bitMapData = outputStream.toByteArray()

    //escrever os bytes no ficheiro
        var fileOutputS: FileOutputStream? = null
            fileOutputS = FileOutputStream(ficheiro)
            fileOutputS?.write(bitMapData)
            fileOutputS?.flush()
            fileOutputS?.close()

        //erro
        } catch (e: IOException) {
            e.printStackTrace()

        }
        return ficheiro
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_report)

        //spinner
        val spntipos =findViewById<Spinner>(R.id.tipos)
        val listtipos : Array<String> = arrayOf(getString(R.string.transito), getString(R.string.obras), getString(R.string.buraco) , getString(R.string.acidente) , getString(R.string.outros))
        val spnadapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listtipos.toList())
        spnadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spntipos.adapter = spnadapter



        val add= findViewById<Button>(R.id.addbtn)
        add.setOnClickListener {

            //imagem
            val imagem = findViewById<ImageView>(R.id.imgadd).drawable.toBitmap()
            val file = convertImg("img", imagem)
            val imgFileRequest: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
            val image: MultipartBody.Part = MultipartBody.Part.createFormData("imagem", file.name, imgFileRequest)

            //valores dos campos
            val titulo = findViewById<EditText>(R.id.editTitulo).text.toString()
            val descricao = findViewById<EditText>(R.id.editdesc).text.toString()
            val latitude = intent.getParcelableExtra<LatLng>("local")!!.latitude
            val longitude = intent.getParcelableExtra<LatLng>("local")!!.longitude
            val tipo = spntipos.selectedItem
            val sharedPref: SharedPreferences = getSharedPreferences(
                    getString(R.string.preference_file_key), Context.MODE_PRIVATE)
            val user: Int = sharedPref.getInt(R.string.shareduser.toString(), 0)

            //valores do request
            val titRequest: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), titulo)
            val descRequest: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), descricao)
            val latRequest: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), latitude.toString())
            val longRequest: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), longitude.toString())
            val useridRequest: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), user.toString())
            val tipoRequest: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), tipo.toString())

    //webservice
            val request = ServiceBuilder.buildService(Endpoints::class.java)

            val call = request.newReport(titRequest, descRequest, latRequest, longRequest, useridRequest, tipoRequest, image)

            call.enqueue(object : Callback<PostOutput> {
                override fun onResponse(call: Call<PostOutput>, response: Response<PostOutput>) {

                    if (response.isSuccessful) {
                        Toast.makeText(this@AddReport, getString(R.string.rport_add_sucesso), Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@AddReport, MapReports::class.java)
                        startActivity(intent)
                        finish()

                    }else{
                        Toast.makeText(this@AddReport, getString(R.string.erro_add_report), Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<PostOutput>, t: Throwable) {
                    Toast.makeText(this@AddReport, "${t.message}", Toast.LENGTH_SHORT).show()
                }
            })


        }

    }


}