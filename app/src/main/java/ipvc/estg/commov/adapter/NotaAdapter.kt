package ipvc.estg.commov.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.commov.EditNota
import ipvc.estg.commov.R
import ipvc.estg.commov.entities.Nota

const val TEXT = "TEXT"
const val ID = "ID"

class NotaAdapter internal constructor(context: Context, private val getid:getId) : RecyclerView.Adapter<NotaAdapter.NotaViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var notas = emptyList<Nota>()

    interface getId {
        fun envia(id: Int)
    }



    class NotaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val notaItemView: TextView = itemView.findViewById(R.id.desc)
        val editar: ImageView = itemView.findViewById(R.id.lapis)
        val apagar: ImageView = itemView.findViewById(R.id.balde)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerline, parent, false)
        return NotaViewHolder(itemView)


    }

    override fun onBindViewHolder(holder: NotaViewHolder, position: Int) {
        val current = notas[position]
        val id : Int?=current.id
        holder.notaItemView.text = current.desc


        holder.apagar.setOnClickListener {
            if (id != null) {
                getid.envia(id)
            }
        }

        holder.editar.setOnClickListener {
            val context = holder.notaItemView.context
            val text = holder.notaItemView.text.toString()


            val intent = Intent ( context, EditNota::class.java).apply{
                putExtra(TEXT, text)
                putExtra(ID, id)

            }
            context.startActivity(intent)
    }

    }

    internal fun setNotas(notas: List<Nota>) {
        this.notas = notas
        notifyDataSetChanged()
    }

    override fun getItemCount() = notas.size


}
