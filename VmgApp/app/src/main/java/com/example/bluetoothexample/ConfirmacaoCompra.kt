package com.example.bluetoothexample

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

class ConfirmacaoCompra : AppCompatActivity() {
    val bcontext = this
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContentView(R.layout.activity_confirmacao_compra)

        val tvAmendoimConfirmacao: TextView = findViewById<TextView>(R.id.tvItem1Confirmacao)
        val tvCastanhaCajuConfirmacao: TextView = findViewById<TextView>(R.id.tvItem2Confirmacao)
        val tvCastanhaParaConfirmacao: TextView = findViewById<TextView>(R.id.tvItem3Confirmacao)

        val bConfirmarCompra: Button = findViewById<Button>(R.id.confirmarCompra)
        val bCancelarCompra: Button = findViewById<Button>(R.id.botaoCancelar)

        tvAmendoimConfirmacao.text = getString(R.string.valorTotalAmendoim).format(SelecaoProdutos.listaDeProdutos[0].peso)
        tvAmendoimConfirmacao.text = getString(R.string.valorTotalCastanhaCaju).format(SelecaoProdutos.listaDeProdutos[1].peso)
        tvAmendoimConfirmacao.text = getString(R.string.valorTotalCastanhaPara).format(SelecaoProdutos.listaDeProdutos[2].peso)
        val tvAvisoPeso: TextView = findViewById<TextView>(R.id.tvAvisoPeso)
        configText()
        configButtons()

        bConfirmarCompra.setOnClickListener{
            /* Enviar info para a VMG e esperar o ACK */
            if(!SelecaoProdutos.modoMisto || SelecaoProdutos.modoMisto &&
                SelecaoProdutos.listaDeProdutos[0].peso +
                SelecaoProdutos.listaDeProdutos[1].peso +
                SelecaoProdutos.listaDeProdutos[2].peso < 900 && TelaConexao.conectado
            ) {
                /* Enviar info para a VMG e esperar o ACK */
                TelaConexao.bluetoothClient.sendPedido()

                /* Tela de "Processo em andamento"? */

                /* Voltar para primeira tela */

                //TelaConexao.bluetoothClient.disconnect()
                val intent = Intent(this@ConfirmacaoCompra, TelaConexao::class.java)

                startActivity(intent)
            }
            else{
                /* TODO: Aviso quanto ao peso máximo */
                tvAvisoPeso.text = "Peso maximo excedido para entrega mista!"
            }

        }

        bCancelarCompra.setOnClickListener{
            /* Limpar todas as informações */

            /* Voltar para primeira tela */

            val intent = Intent(this@ConfirmacaoCompra, TelaConexao::class.java)

            startActivity(intent)
        }
    }


    override fun onResume() {
        super.onResume()

        setContentView(R.layout.activity_confirmacao_compra)

        val tvAmendoimConfirmacao: TextView = findViewById<TextView>(R.id.tvItem1Confirmacao)
        val tvCastanhaCajuConfirmacao: TextView = findViewById<TextView>(R.id.tvItem2Confirmacao)
        val tvCastanhaParaConfirmacao: TextView = findViewById<TextView>(R.id.tvItem3Confirmacao)
        val tvAvisoPeso: TextView = findViewById<TextView>(R.id.tvAvisoPeso)

        val bConfirmarCompra: Button = findViewById<Button>(R.id.confirmarCompra)
        val bCancelarCompra: Button = findViewById<Button>(R.id.botaoCancelar)

        configText()
        configButtons()

        var escolhido1 = 0
        var escolhido2 = 0
        var escolhido3 = 0

        if(SelecaoProdutos.listaDeProdutos[0].escolhido)
            escolhido1 = 1
        if(SelecaoProdutos.listaDeProdutos[1].escolhido)
            escolhido2 = 1
        if(SelecaoProdutos.listaDeProdutos[2].escolhido)
            escolhido3 = 1

        tvAmendoimConfirmacao.text = getString(R.string.valorTotalAmendoim).format(SelecaoProdutos.listaDeProdutos[0].peso * escolhido1)
        tvCastanhaCajuConfirmacao.text = getString(R.string.valorTotalCastanhaCaju).format(SelecaoProdutos.listaDeProdutos[1].peso * escolhido2)
        tvCastanhaParaConfirmacao.text = getString(R.string.valorTotalCastanhaPara).format(SelecaoProdutos.listaDeProdutos[2].peso * escolhido3)

        bConfirmarCompra.setOnClickListener{
            if(SelecaoProdutos.listaDeProdutos[0].peso * escolhido1
                > SelecaoProdutos.listaDeProdutos[0].pesoDisponivel ||
                SelecaoProdutos.listaDeProdutos[1].peso  * escolhido2
                > SelecaoProdutos.listaDeProdutos[1].pesoDisponivel ||
                SelecaoProdutos.listaDeProdutos[2].peso  * escolhido3
                > SelecaoProdutos.listaDeProdutos[2].pesoDisponivel)
            {
                if(SelecaoProdutos.listaDeProdutos[0].peso  * escolhido1
                    > SelecaoProdutos.listaDeProdutos[0].pesoDisponivel)
                {
                    tvAvisoPeso.text = "Produto indisponível: Amendoim\n" +
                            "Peso disponível: %d".format(SelecaoProdutos.listaDeProdutos[0].pesoDisponivel)
                }
                if(SelecaoProdutos.listaDeProdutos[1].peso  * escolhido1
                    > SelecaoProdutos.listaDeProdutos[1].pesoDisponivel)
                {
                    tvAvisoPeso.text = "Produto indisponível: Castanha do Pará\n" +
                            "Peso disponível: %d".format(SelecaoProdutos.listaDeProdutos[1].pesoDisponivel)
                }
                if(SelecaoProdutos.listaDeProdutos[2].peso  * escolhido1
                    > SelecaoProdutos.listaDeProdutos[2].pesoDisponivel)
                {
                    tvAvisoPeso.text = "Produto indisponível: Castanha de Caju\n" +
                            "Peso disponível: %d".format(SelecaoProdutos.listaDeProdutos[2].pesoDisponivel)
                }
            }
            /* Dinheiro suficiente? */
            else if(SelecaoProdutos.listaDeProdutos[0].peso
                * SelecaoProdutos.listaDeProdutos[0].custo / 1000 * escolhido1 +
                SelecaoProdutos.listaDeProdutos[1].peso
                * SelecaoProdutos.listaDeProdutos[1].custo / 1000 * escolhido2 +
                SelecaoProdutos.listaDeProdutos[2].peso
                * SelecaoProdutos.listaDeProdutos[2].custo / 1000 * escolhido3
                > TelaConexao.usuario.creditos)
            {
                tvAvisoPeso.text = "Dinheiro insuficiente!"

            }
            /* Caso os pesos sejam menores que 900g */
            else if(!SelecaoProdutos.modoMisto || SelecaoProdutos.modoMisto &&
                    SelecaoProdutos.listaDeProdutos[0].peso +
                    SelecaoProdutos.listaDeProdutos[1].peso +
                    SelecaoProdutos.listaDeProdutos[2].peso < 900 &&
                TelaConexao.conectado
                    ) {
                /* Enviar info para a VMG e esperar o ACK */
                TelaConexao.bluetoothClient.sendPedido()

                /* Retira créditos do usuário */
                TelaConexao.usuario.creditos -= (SelecaoProdutos.listaDeProdutos[0].peso
                * SelecaoProdutos.listaDeProdutos[0].custo / 1000 +
                        SelecaoProdutos.listaDeProdutos[1].peso
                * SelecaoProdutos.listaDeProdutos[1].custo / 1000 +
                        SelecaoProdutos.listaDeProdutos[2].peso
                * SelecaoProdutos.listaDeProdutos[2].custo / 1000)

                /* Voltar para primeira tela */
                    val intent = Intent(this@ConfirmacaoCompra, TelaConexao::class.java)

                    startActivity(intent)

            }
            else{
                if(TelaConexao.conectado == false) {
                    tvAvisoPeso.text = "Não conectado!"

                }
                else {
                    /* TODO: Aviso quanto ao peso máximo */
                    tvAvisoPeso.text = "Peso maximo excedido para entrega mista!"
                }
            }
        }

        bCancelarCompra.setOnClickListener{
            /* Limpar todas as informações */
            /* Voltar para primeira tela */
            val intent = Intent(this@ConfirmacaoCompra, TelaConexao::class.java)

            startActivity(intent)
        }
    }

    fun configButtons()
    {
        val bModoMisto = findViewById<Button>(R.id.botaoModoMisto)

        bModoMisto.setOnClickListener {
            SelecaoProdutos.modoMisto = !SelecaoProdutos.modoMisto
            configText()
        }
    }

    fun configText()
    {
        val tvModoMisto = findViewById<TextView>(R.id.modoMisto)

        if(SelecaoProdutos.modoMisto == false)
            tvModoMisto.text = "Entregar Separado"
        if(SelecaoProdutos.modoMisto == true)
            tvModoMisto.text = "Entregar Junto"

    }


}