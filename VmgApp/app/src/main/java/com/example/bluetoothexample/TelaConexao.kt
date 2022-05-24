package com.example.bluetoothexample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class TelaConexao : AppCompatActivity() {
    companion object TelaConexao{
        val usuario : Usuario = Usuario(0.0F, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_conexao)


        val buttonConfirmarConexao:Button = findViewById(R.id.confirmConnect)
        buttonConfirmarConexao.setOnClickListener{
            val intent = Intent(this@TelaConexao, SelecaoProdutos::class.java)
            resetListaCompras()
            startActivity(intent)
        }

        val buttonAdicaoCreditos = findViewById<Button>(R.id.botaoAdicionarCreditos)
        buttonAdicaoCreditos.setOnClickListener{
            val intent = Intent(this@TelaConexao, AdicaoCreditos::class.java)
            startActivity(intent)
        }



    }

    fun resetListaCompras()
    {
        SelecaoProdutos.listaDeProdutos[0].peso = 0
        SelecaoProdutos.listaDeProdutos[0].escolhido = false

        SelecaoProdutos.listaDeProdutos[1].peso = 0
        SelecaoProdutos.listaDeProdutos[1].escolhido = false

        SelecaoProdutos.listaDeProdutos[2].peso = 0
        SelecaoProdutos.listaDeProdutos[2].escolhido = false

    }
}