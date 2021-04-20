package ipvc.estg.commov

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import ipvc.estg.commov.api.Endpoints
import ipvc.estg.commov.api.ServiceBuilder
import ipvc.estg.commov.api.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val notabtn = findViewById<Button>(R.id.notabtn)
        notabtn.setOnClickListener {
            val intent = Intent(this, Notas::class.java)
            startActivity(intent)
        }
        val loginbtn = findViewById<Button>(R.id.login)
        loginbtn.setOnClickListener { login(it) }
    }


    fun login(view: View) {
        val emailtext = findViewById<EditText>(R.id.getemail)
        val passtext = findViewById<EditText>(R.id.getpass)

        val request = ServiceBuilder.buildService(Endpoints::class.java)
        val call = request.getUserByEmail(emailtext.text.toString())
        val intent = Intent(this, MapsReport::class.java)

        if (emailtext.text.toString().isNullOrEmpty() && passtext.text.toString().isNullOrEmpty()) {
            Toast.makeText(applicationContext, getString(R.string.email_pass_empy), Toast.LENGTH_SHORT).show()
            emailtext.requestFocus()
            passtext.requestFocus()
        }

        if (!emailtext.text.toString().isNullOrEmpty() && passtext.text.toString().isNullOrEmpty()) {
            Toast.makeText(applicationContext, getString(R.string.email_vazio_error), Toast.LENGTH_SHORT).show()
            emailtext.requestFocus()
        }

        if (emailtext.text.toString().isNullOrEmpty() && !passtext.text.toString().isNullOrEmpty()) {
            Toast.makeText(applicationContext, getString(R.string.pass_vazia_Error), Toast.LENGTH_SHORT).show()
            passtext.requestFocus()
        }


        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val c: User = response.body()!!


                    if (emailtext.text.toString().equals(c.email) && (passtext.text.toString().equals(c.password))) {
                        startActivity(intent)
                    } else if (!(emailtext.text.toString().equals(c.email) && (passtext.text.toString().equals(c.password)))) {
                        Toast.makeText(applicationContext, getString(R.string.email_pass_error), Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

    }


}
    //teste commit
