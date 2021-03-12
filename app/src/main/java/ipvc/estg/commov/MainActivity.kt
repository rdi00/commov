package ipvc.estg.commov

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun notabtn(view: View) {
        val intent = Intent (this, notas::class.java)
        startActivity(intent)
    }


    //teste commit
}