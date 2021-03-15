package ipvc.estg.commov

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val notabtn = findViewById<Button>(R.id.notabtn)
        notabtn.setOnClickListener{
            val intent = Intent (this, Notas::class.java)
            startActivity(intent)
        }
    }




    //teste commit
}