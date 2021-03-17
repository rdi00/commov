package ipvc.estg.commov

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.commov.adapter.NotaAdapter
import ipvc.estg.commov.entities.Nota
import ipvc.estg.commov.viewModel.NotaViewModel


class Notas : AppCompatActivity(), NotaAdapter.getId {

    private lateinit var notaViewModel: NotaViewModel
    private val newWordActivityRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notas)

        // recycler view
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val adapter = NotaAdapter(this, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // view model
        notaViewModel = ViewModelProvider(this).get(NotaViewModel::class.java)
        notaViewModel.allNotas.observe(this, Observer { notas ->
            // Update the cached copy of the words in the adapter.
            notas?.let { adapter.setNotas(it) }
        })

        //add
        val add = findViewById<Button>(R.id.button)
        add.setOnClickListener {
            val intent = Intent(this@Notas, AddNota::class.java)
            startActivityForResult(intent, newWordActivityRequestCode)
        }


    }

    override fun envia (id: Int){
            notaViewModel.deleteById(id)
        Toast.makeText(applicationContext, "Apagou Nota", Toast.LENGTH_SHORT).show()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {

            val pdesc = data?.getStringExtra(AddNota.EXTRA_REPLY_DESC)

            if ( pdesc != null) {
                val nota = Nota(desc = pdesc)
                notaViewModel.insert(nota)
            }

        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG).show()
        }
    }



}