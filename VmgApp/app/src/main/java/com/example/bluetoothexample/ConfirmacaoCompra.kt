package com.example.bluetoothexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import org.w3c.dom.Text

class ConfirmacaoCompra : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmacao_compra)

        val tvAmendoimConfirmacao: TextView = findViewById<TextView>(R.id.tvItem1Confirmacao)
        val tvCastanhaCajuConfirmacao: TextView = findViewById<TextView>(R.id.tvItem2Confirmacao)
        val tvCastanhaParaConfirmacao: TextView = findViewById<TextView>(R.id.tvItem3Confirmacao)

        tvAmendoimConfirmacao.text = "Amendoim: %d g".format(SelecaoProdutos.listaDeProdutos[0].peso)
        tvAmendoimConfirmacao.text = "Castanha de Caju: %d g".format(SelecaoProdutos.listaDeProdutos[1].peso)
        tvAmendoimConfirmacao.text = "Castanha do Pará: %d g".format(SelecaoProdutos.listaDeProdutos[2].peso)




    }




}