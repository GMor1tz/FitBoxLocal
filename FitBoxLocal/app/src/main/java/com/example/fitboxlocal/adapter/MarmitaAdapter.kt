package com.example.fitboxlocal.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fitboxlocal.databinding.ItemMarmitaBinding
import com.example.fitboxlocal.model.Marmita
import java.util.Locale

class MarmitaAdapter(
    private var listaMarmitas: List<Marmita>,
    private val onEditarClick: (Marmita) -> Unit,
    private val onExcluirClick: (Marmita) -> Unit
) : RecyclerView.Adapter<MarmitaAdapter.MarmitaViewHolder>() {

    inner class MarmitaViewHolder(val binding: ItemMarmitaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarmitaViewHolder {
        val binding = ItemMarmitaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MarmitaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MarmitaViewHolder, position: Int) {
        val marmita = listaMarmitas[position]
        with(holder.binding) {
            tvNome.text = marmita.nome
            tvCategoria.text = marmita.categoria
            val precoFormatado = String.format(Locale("pt", "BR"), "R$ %.2f", marmita.preco)
            tvPrecoNutricao.text = "$precoFormatado • ${marmita.calorias} kcal • ${marmita.proteinaGramas}g Prot."
            tvDisponibilidade.text = "Status: ${marmita.disponibilidade}"

            btnEditar.setOnClickListener { onEditarClick(marmita) }
            btnExcluir.setOnClickListener { onExcluirClick(marmita) }
        }
    }

    override fun getItemCount(): Int = listaMarmitas.size

    fun atualizarLista(novaLista: List<Marmita>) {
        listaMarmitas = novaLista
        notifyDataSetChanged()
    }
}