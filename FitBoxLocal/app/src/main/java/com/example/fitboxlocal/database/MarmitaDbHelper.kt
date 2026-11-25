package com.example.fitboxlocal.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.fitboxlocal.model.Marmita

class MarmitaDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "fitbox_local.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_MARMITAS = "marmitas"

        private const val COL_ID = "id"
        private const val COL_NOME = "nome"
        private const val COL_CATEGORIA = "categoria"
        private const val COL_CALORIAS = "calorias"
        private const val COL_PROTEINA = "proteina_gramas"
        private const val COL_PRECO = "preco"
        private const val COL_DISPONIBILIDADE = "disponibilidade"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_MARMITAS (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NOME TEXT NOT NULL,
                $COL_CATEGORIA TEXT NOT NULL,
                $COL_CALORIAS INTEGER NOT NULL,
                $COL_PROTEINA INTEGER NOT NULL,
                $COL_PRECO REAL NOT NULL,
                $COL_DISPONIBILIDADE TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MARMITAS")
        onCreate(db)
    }

    fun inserirMarmita(marmita: Marmita): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_NOME, marmita.nome)
            put(COL_CATEGORIA, marmita.categoria)
            put(COL_CALORIAS, marmita.calorias)
            put(COL_PROTEINA, marmita.proteinaGramas)
            put(COL_PRECO, marmita.preco)
            put(COL_DISPONIBILIDADE, marmita.disponibilidade)
        }
        val id = db.insert(TABLE_MARMITAS, null, values)
        db.close()
        return id
    }

    fun listarMarmitas(): List<Marmita> {
        val lista = mutableListOf<Marmita>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_MARMITAS ORDER BY $COL_ID DESC", null)

        if (cursor.moveToFirst()) {
            do {
                val marmita = Marmita(
                    id = cursor.getLong(cursor.getColumnIndexOrThrow(COL_ID)),
                    nome = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOME)),
                    categoria = cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORIA)),
                    calorias = cursor.getInt(cursor.getColumnIndexOrThrow(COL_CALORIAS)),
                    proteinaGramas = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PROTEINA)),
                    preco = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PRECO)),
                    disponibilidade = cursor.getString(cursor.getColumnIndexOrThrow(COL_DISPONIBILIDADE))
                )
                lista.add(marmita)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }

    fun atualizarMarmita(marmita: Marmita): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_NOME, marmita.nome)
            put(COL_CATEGORIA, marmita.categoria)
            put(COL_CALORIAS, marmita.calorias)
            put(COL_PROTEINA, marmita.proteinaGramas)
            put(COL_PRECO, marmita.preco)
            put(COL_DISPONIBILIDADE, marmita.disponibilidade)
        }
        val linhas = db.update(TABLE_MARMITAS, values, "$COL_ID = ?", arrayOf(marmita.id.toString()))
        db.close()
        return linhas
    }

    fun excluirMarmita(id: Long): Int {
        val db = writableDatabase
        val linhas = db.delete(TABLE_MARMITAS, "$COL_ID = ?", arrayOf(id.toString()))
        db.close()
        return linhas
    }
}