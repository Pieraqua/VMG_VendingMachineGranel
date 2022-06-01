package com.example.bluetoothexample

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class AdicaoCreditos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicao_creditos)

        setupBotoes()
        setupTextos()
    }

    override fun onResume() {
        super.onResume()
        setContentView(R.layout.activity_adicao_creditos)

        setupBotoes()
        setupTextos()
    }

    private fun setupBotoes() {
        val bOk: Button = findViewById<Button>(R.id.button_adicaoCreditos)
        val bVoltar: Button = findViewById<Button>(R.id.button_voltar)

        bOk.setOnClickListener {
            /* Adiciona créditos na VMG */
            adicionarCreditos()

            /* Volta para a tela inicial */
            val intent = Intent(this@AdicaoCreditos, TelaConexao::class.java)

            startActivity(intent)

        }

        bVoltar.setOnClickListener {
            val intent = Intent(this@AdicaoCreditos, TelaConexao::class.java)

            startActivity(intent)
        }

    }

    private fun adicionarCreditos() {
        val codigoPromocional = findViewById<EditText>(R.id.inputCreditos).text.toString()

        if(codigoPromocional == "abcde")
            TelaConexao.bluetoothClient.sendPacoteCredito(100)

    }

    private fun getCreditosAtuais() : Float{
        var creditosAtuais = TelaConexao.usuario.creditos

        return creditosAtuais
    }

    private fun setupTextos()
    {
        val tvCreditos = findViewById<TextView>(R.id.creditosAtuais)

        val texto = String.format("R$%.02f", TelaConexao.usuario.creditos)
        tvCreditos.text = texto
    }
}