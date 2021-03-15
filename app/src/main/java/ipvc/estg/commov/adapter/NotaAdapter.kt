package ipvc.estg.commov.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.commov.EditNota
import ipvc.estg.commov.Notas
import ipvc.estg.commov.R
import ipvc.estg.commov.entities.Nota
import ipvc.estg.commov.viewModel.NotaViewModel


const val DESC ="DESC"

class NotaAdapter internal constructor( context: Context) : RecyclerView.Adapter<NotaAdapter.NotaViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var notas = emptyList<Nota>()


    class NotaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val notaItemView: TextView = itemView.findViewById(R.id.desc)
        val rmicon: ImageButton = itemView.findViewById(R.id.balde)
        val editicon: ImageButton = itemView.findViewById(R.id.lapis)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerline, parent, false)
        return NotaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NotaViewHolder, position: Int) {
        val current = notas[position]
        holder.notaItemView.text = current.id.toString() + "-" + current.desc
        val id = current.id


        holder.editicon.setOnClickListener {
            val context = holder.notaItemView.context
            val desc = holder.notaItemView.text.toString()


            val intent = Intent(context, EditNota::class.java).apply {
                putExtra(DESC, desc)
            }
            context.startActivity(intent)
        }

        holder.rmicon.setOnClickListener {
            id
        }
    }

    internal fun setNotas(notas: List<Nota>) {
        this.notas = notas
        notifyDataSetChanged()
    }

    override fun getItemCount() = notas.size
}