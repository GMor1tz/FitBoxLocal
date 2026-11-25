package com.example.fitboxlocal

import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fitboxlocal.database.MarmitaDbHelper
import com.example.fitboxlocal.databinding.ActivityFormularioBinding
import com.example.fitboxlocal.model.Marmita

class FormularioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormularioBinding
    private lateinit var dbHelper: MarmitaDbHelper
    private var marmitaEmEdicao: Marmita? = null

    private val categorias = listOf("Tradicional Fit", "Hipertrofia", "Low Carb", "Kit Semanal", "Vegetariana")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormularioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = MarmitaDbHelper(this)
        configurarSpinner()
        verificarModoEdicao()

        binding.btnSalvar.setOnClickListener {
            salvarMarmita()
        }
    }

    private fun configurarSpinner() {
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categorias)
        binding.spinnerCategoria.adapter = spinnerAdapter
    }

    private fun verificarModoEdicao() {
        marmitaEmEdicao = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("MARMITA_EDITAR", Marmita::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("MARMITA_EDITAR") as? Marmita
        }

        marmitaEmEdicao?.let { marmita ->
            binding.tvTituloTela.text = "Editar Marmita"
            binding.btnSalvar.text = "Atualizar no Catálogo"
            binding.etNome.setText(marmita.nome)
            binding.etCalorias.setText(marmita.calorias.toString())
            binding.etProteina.setText(marmita.proteinaGramas.toString())
            binding.etPreco.setText(marmita.preco.toString())
            binding.etDisponibilidade.setText(marmita.disponibilidade)

            val posicao = categorias.indexOf(marmita.categoria)
            if (posicao >= 0) binding.spinnerCategoria.setSelection(posicao)
        }
    }

    private fun salvarMarmita() {
        val nome = binding.etNome.text.toString().trim()
        val caloriasStr = binding.etCalorias.text.toString().trim()
        val proteinaStr = binding.etProteina.text.toString().trim()
        val precoStr = binding.etPreco.text.toString().replace(",", ".").trim()
        val disponibilidade = binding.etDisponibilidade.text.toString().trim()
        val categoria = binding.spinnerCategoria.selectedItem.toString()

        if (nome.isEmpty() || caloriasStr.isEmpty() || proteinaStr.isEmpty() || precoStr.isEmpty() || disponibilidade.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            return
        }

        val calorias = caloriasStr.toIntOrNull()
        val proteina = proteinaStr.toIntOrNull()
        val preco = precoStr.toDoubleOrNull()

        if (calorias == null || calorias <= 0 || proteina == null || proteina < 0 || preco == null || preco <= 0) {
            Toast.makeText(this, "Informe valores numéricos válidos!", Toast.LENGTH_SHORT).show()
            return
        }

        if (marmitaEmEdicao == null) {
            val novaMarmita = Marmita(
                nome = nome,
                categoria = categoria,
                calorias = calorias,
                proteinaGramas = proteina,
                preco = preco,
                disponibilidade = disponibilidade
            )
            dbHelper.inserirMarmita(novaMarmita)
            Toast.makeText(this, "Marmita adicionada ao catálogo!", Toast.LENGTH_SHORT).show()
        } else {
            val marmitaAtualizada = Marmita(
                id = marmitaEmEdicao!!.id,
                nome = nome,
                categoria = categoria,
                calorias = calorias,
                proteinaGramas = proteina,
                preco = preco,
                disponibilidade = disponibilidade
            )
            dbHelper.atualizarMarmita(marmitaAtualizada)
            Toast.makeText(this, "Marmita atualizada!", Toast.LENGTH_SHORT).show()
        }

        finish()
    }
}