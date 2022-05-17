package com.example.bluetoothexample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class listaProdutosAdapter(private val listaProdutos: Array<Produto>):
    RecyclerView.Adapter<listaProdutosAdapter.ViewHolder>()
{
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val textView: TextView

        init{
            textView = view.findViewById(R.id.texto_produto)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.lista_produtos_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = listaProdutos[position].nome
    }

    override fun getItemCount(): Int {
        return listaProdutos.size
    }
}