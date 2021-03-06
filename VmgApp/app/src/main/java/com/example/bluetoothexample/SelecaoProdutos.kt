package com.example.bluetoothexample

import android.bluetooth.BluetoothManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class SelecaoProdutos : AppCompatActivity() {
    companion object ListaProdutos{
        val listaDeProdutos: Array<Produto> =
            Array(3) { Produto(nome = "", peso = 100, escolhido = false, custo = 0, pesoDisponivel = 0) }
        var modoMisto : Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selecao_produtos)
        val buttonConfirmarEscolha = findViewById<Button>(R.id.confirm_button_produtos)

        resetListaCompras()
        displayEscolhidos()

        buttonConfirmarEscolha.setOnClickListener{
            /* Se algum produto foi escolhido */
            if(listaDeProdutos[0].escolhido || listaDeProdutos[1].escolhido || listaDeProdutos[2].escolhido)
            {
                /* Pode ir pra seleção de pesos */
                val intent = Intent(this@SelecaoProdutos, SelecaoPesos::class.java)

                startActivity(intent)
            }

        }


    }

    override fun onResume() {
        super.onResume()
        setContentView(R.layout.activity_selecao_produtos)
        val buttonConfirmarEscolha = findViewById<Button>(R.id.confirm_button_produtos)

        resetListaCompras()
        displayEscolhidos()

        buttonConfirmarEscolha.setOnClickListener{
            /* Se algum produto foi escolhido */
            if(listaDeProdutos[0].escolhido || listaDeProdutos[1].escolhido || listaDeProdutos[2].escolhido)
            {
                /* Pode ir pra seleção de pesos */
                val intent = Intent(this@SelecaoProdutos, SelecaoPesos::class.java)

                startActivity(intent)
            }

        }
    }

    fun resetListaCompras()
    {
        SelecaoProdutos.listaDeProdutos[0].peso = 100

        SelecaoProdutos.listaDeProdutos[1].peso = 100

        SelecaoProdutos.listaDeProdutos[2].peso = 100

    }

    fun displayEscolhidos()
    {
        val textItem1 = findViewById<TextView>(R.id.tvItem1)
        val textItem2 = findViewById<TextView>(R.id.tvItem2)
        val textItem3 = findViewById<TextView>(R.id.tvItem3)

        val buttonAmendoim = findViewById<Button>(R.id.item1)
        val buttonCastanhaCaju = findViewById<Button>(R.id.item2)
        val buttonCastanhaPara = findViewById<Button>(R.id.item3)

        listaDeProdutos[0].nome = "Amendoim"
        listaDeProdutos[1].nome = "Castanha de Caju"
        listaDeProdutos[2].nome = "Castanha do Pará"
        listaDeProdutos[0].custo = 30
        listaDeProdutos[1].custo = 60
        listaDeProdutos[2].custo = 90

        if(listaDeProdutos[0].escolhido)
        {
            textItem1.text = listaDeProdutos[0].nome
        }
        else
        {
            textItem1.text = ""
        }

        if(listaDeProdutos[1].escolhido)
        {
            textItem2.text = listaDeProdutos[1].nome
        }
        else
        {
            textItem2.text = ""
        }

        if(listaDeProdutos[2].escolhido)
        {
            textItem3.text = listaDeProdutos[2].nome
        }
        else
        {
            textItem3.text = ""
        }

        buttonAmendoim.setOnClickListener{
            val itemName = "Amendoim"

            listaDeProdutos[0].escolhido = !listaDeProdutos[0].escolhido

            if(listaDeProdutos[0].escolhido)
            {
                textItem1.text = itemName
            }
            else
            {
                textItem1.text = ""
            }
        }

        buttonCastanhaCaju.setOnClickListener{
            val itemName = "Castanha de Caju"

            listaDeProdutos[1].escolhido = !listaDeProdutos[1].escolhido
            if(listaDeProdutos[1].escolhido)
            {
                textItem2.text = itemName
            }
            else
            {
                textItem2.text = ""
            }
        }

        buttonCastanhaPara.setOnClickListener{
            val itemName = "Castanha do Pará"

            listaDeProdutos[2].escolhido = !listaDeProdutos[2].escolhido
            if(listaDeProdutos[2].escolhido)
            {
                textItem3.text = itemName
            }
            else
            {
                textItem3.text = ""
            }

        }
    }

}