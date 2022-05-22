package com.example.bluetoothexample

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class TelaConexao : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_conexao)

        val buttonConfirmarConexao:Button = findViewById(R.id.confirmConnect)
        buttonConfirmarConexao.setOnClickListener{
            val intent = Intent(this@TelaConexao, SelecaoProdutos::class.java)
            resetListaCompras()
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