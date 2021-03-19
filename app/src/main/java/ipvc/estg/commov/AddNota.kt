package ipvc.estg.commov

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class AddNota : AppCompatActivity() {


    private lateinit var descText: EditText
    private lateinit var titText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_nota)


        descText = findViewById(R.id.desc)
        titText = findViewById(R.id.tit)

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(descText.text)  ) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            }
            if( TextUtils.isEmpty(titText.text)  ) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            }

            else {
                replyIntent.putExtra(EXTRA_REPLY_DESC, descText.text.toString())
                replyIntent.putExtra(EXTRA_REPLY_TIT, titText.text.toString())

                setResult(Activity.RESULT_OK, replyIntent)
                Toast.makeText(applicationContext, "Inseriu Nota", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY_DESC = "com.example.android.desc"
        const val EXTRA_REPLY_TIT = "com.example.android.tit"
    }
}