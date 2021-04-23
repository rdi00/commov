package ipvc.estg.commov

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

        val sharedPref: SharedPreferences = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
        val login =sharedPref.getBoolean(getString(R.string.sharedlogin), false)
        val email = sharedPref.getString(getString(R.string.sharedemail), "")
        val id = sharedPref.getInt(getString(R.string.shareduser), 0)

        if(sharedPref != null) {
        if(login == true){
            val intent = Intent(this, MapReports::class.java)
            startActivity(intent)
        }
        }

        val notabtn = findViewById<Button>(R.id.notabtn)
        notabtn.setOnClickListener {
            val intent = Intent(this, Notas::class.java)
            startActivity(intent)
        }
    }

    fun login(view: View) {
        val emailtext = findViewById<EditText>(R.id.getemail)
        val passtext = findViewById<EditText>(R.id.getpass)

        val request = ServiceBuilder.buildService(Endpoints::class.java)
        val call = request.getUserByEmail(emailtext.text.toString())
        val intent = Intent(this, MapReports::class.java)

        if (emailtext.text.toString().isNullOrEmpty() && passtext.text.toString().isNullOrEmpty()) {
            Toast.makeText(
                applicationContext,
                getString(R.string.email_pass_empy),
                Toast.LENGTH_SHORT
            ).show()
            emailtext.requestFocus()
            passtext.requestFocus()
        }

        if (emailtext.text.toString().isNullOrEmpty() && !passtext.text.toString()
                .isNullOrEmpty()
        ) {
            Toast.makeText(
                applicationContext,
                getString(R.string.email_vazio_error),
                Toast.LENGTH_SHORT
            ).show()
            emailtext.requestFocus()
        }

        if (!emailtext.text.toString().isNullOrEmpty() && passtext.text.toString()
                .isNullOrEmpty()
        ) {
            Toast.makeText(
                applicationContext,
                getString(R.string.pass_vazia_Error),
                Toast.LENGTH_SHORT
            ).show()
            passtext.requestFocus()
        } else {
            call.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        val u: User = response.body()!!

                        if (emailtext.text.toString().equals(u.email) && (passtext.text.toString()
                                .equals(u.password))
                        ) {
                            startActivity(intent)

                            val sharedPref: SharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                            with(sharedPref.edit()){
                                putBoolean(getString(R.string.sharedlogin), true)
                                putString(getString(R.string.sharedemail),"${emailtext.text}")
                                putInt(getString(R.string.shareduser), u.id)
                                Log.d("*****Valores", "$")
                                commit()
                            }

                        } else if (!(emailtext.text.toString()
                                .equals(u.email) && (passtext.text.toString().equals(u.password)))
                        ) {
                            Toast.makeText(
                                applicationContext,
                                getString(R.string.email_pass_error),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
                        override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                }
                }

            ) }

    }
}
    //teste commit
