package ipvc.estg.commov

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import ipvc.estg.commov.adapter.ID
import ipvc.estg.commov.adapter.NotaAdapter
import ipvc.estg.commov.adapter.TEXT
import ipvc.estg.commov.viewModel.NotaViewModel
import kotlin.math.absoluteValue

class EditNota : AppCompatActivity() {

    private lateinit var notaViewModel: NotaViewModel
    private lateinit var DESC: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_nota)
        val valor: String? = intent.extras?.getString(TEXT)
        val idpass: Int? =intent.extras?.getInt(ID)

        notaViewModel = ViewModelProvider(this).get(NotaViewModel::class.java)

        DESC = findViewById(R.id.DESC)
        DESC.setText(valor)

        val btnedit = findViewById<Button>(R.id.button_edit)
        btnedit.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(DESC.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
                Toast.makeText(applicationContext, "Não edita", Toast.LENGTH_SHORT).show()
            } else {
                if (idpass != null) {
                    notaViewModel.update(DESC.text.toString(), idpass)
                    Toast.makeText(applicationContext, "Alterou Nota", Toast.LENGTH_SHORT).show()
                }

            }
            finish()
        }
    }

}