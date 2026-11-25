package com.example.fitboxlocal

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitboxlocal.adapter.MarmitaAdapter
import com.example.fitboxlocal.database.MarmitaDbHelper
import com.example.fitboxlocal.databinding.ActivityMainBinding
import com.example.fitboxlocal.model.Marmita

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dbHelper: MarmitaDbHelper
    private lateinit var adapter: MarmitaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = MarmitaDbHelper(this)
        configurarRecyclerView()

        binding.fabAdicionar.setOnClickListener {
            val intent = Intent(this, FormularioActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        carregarMarmitas()
    }

    private fun configurarRecyclerView() {
        adapter = MarmitaAdapter(
            listaMarmitas = emptyList(),
            onEditarClick = { marmita ->
                val intent = Intent(this, FormularioActivity::class.java)
                intent.putExtra("MARMITA_EDITAR", marmita)
                startActivity(intent)
            },
            onExcluirClick = { marmita ->
                confirmarExclusao(marmita)
            }
        )
        binding.recyclerViewMarmitas.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewMarmitas.adapter = adapter
    }

    private fun carregarMarmitas() {
        val marmitas = dbHelper.listarMarmitas()
        adapter.atualizarLista(marmitas)

        if (marmitas.isEmpty()) {
            binding.tvListaVazia.visibility = View.VISIBLE
            binding.recyclerViewMarmitas.visibility = View.GONE
        } else {
            binding.tvListaVazia.visibility = View.GONE
            binding.recyclerViewMarmitas.visibility = View.VISIBLE
        }
    }

    private fun confirmarExclusao(marmita: Marmita) {
        AlertDialog.Builder(this)
            .setTitle("Remover prato")
            .setMessage("Deseja realmente excluir \"${marmita.nome}\" do cardápio?")
            .setPositiveButton("Excluir") { _, _ ->
                dbHelper.excluirMarmita(marmita.id)
                carregarMarmitas()
                Toast.makeText(this, "Prato removido do catálogo!", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}