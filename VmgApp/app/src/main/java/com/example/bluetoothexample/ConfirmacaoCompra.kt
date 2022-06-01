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
                SelecaoProdutos.listaDeProdutos[2].peso < 900 && TelaConexao.bluetoothClient.getConnected()
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

    fun write()
    {
        if(TelaConexao.bluetoothClient.mmSocket != null) {
            TelaConexao.bluetoothClient.mmSocket!!.outputStream.write("Abobrinha".toByteArray())
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

        tvAmendoimConfirmacao.text = getString(R.string.valorTotalAmendoim).format(SelecaoProdutos.listaDeProdutos[0].peso)
        tvCastanhaCajuConfirmacao.text = getString(R.string.valorTotalCastanhaCaju).format(SelecaoProdutos.listaDeProdutos[1].peso)
        tvCastanhaParaConfirmacao.text = getString(R.string.valorTotalCastanhaPara).format(SelecaoProdutos.listaDeProdutos[2].peso)

        bConfirmarCompra.setOnClickListener{

            if(!SelecaoProdutos.modoMisto || SelecaoProdutos.modoMisto &&
                    SelecaoProdutos.listaDeProdutos[0].peso +
                    SelecaoProdutos.listaDeProdutos[1].peso +
                    SelecaoProdutos.listaDeProdutos[2].peso < 900 &&
                TelaConexao.bluetoothClient.getConnected()
                    ) {
                /* Enviar info para a VMG e esperar o ACK */
                TelaConexao.bluetoothClient.sendPedido()

                /* Tela de "Processo em andamento"? */

                /* Voltar para primeira tela */
                    val intent = Intent(this@ConfirmacaoCompra, TelaConexao::class.java)

                    startActivity(intent)

            }
            else{
                if(TelaConexao.bluetoothClient.getConnected() == false) {
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
            TelaConexao.bluetoothClient.cancel()

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