package com.example.fitboxlocal.model

import java.io.Serializable

data class Marmita(
    val id: Long = 0,
    val nome: String,
    val categoria: String,
    val calorias: Int,
    val proteinaGramas: Int,
    val preco: Double,
    val disponibilidade: String
) : Serializable