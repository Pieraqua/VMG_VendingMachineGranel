package com.example.bluetoothexample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class SelecaoPesos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selecao_pesos)

        val botaoMais50 = findViewById<Button>(R.id.bMais50g)
        val botaoMenos50 = findViewById<Button>(R.id.bMenos50g)
        val botaoConfirmarPeso = findViewById<Button>(R.id.bConfirmarPeso)

        val tvItemAtual = findViewById<TextView>(R.id.tvItemAtual)
        val tvPesoAtual = findViewById<TextView>(R.id.tvPesoAtual)

        var produtoAtual = 0



        while(!SelecaoProdutos.listaDeProdutos[produtoAtual].escolhido)
        {
            produtoAtual++

            if(produtoAtual >= 3)
            {
                /* Nenhum produto escolhido? */
                //TODO: tratar erro

                val intent = Intent(this@SelecaoPesos, ConfirmacaoCompra::class.java)

                startActivity(intent)
            }
        }

        tvPesoAtual.setText(String.format("%d g",
            SelecaoProdutos.listaDeProdutos[produtoAtual].peso))
        tvItemAtual.text = SelecaoProdutos.listaDeProdutos[produtoAtual].nome

        botaoMais50.setOnClickListener{
            if(SelecaoProdutos.listaDeProdutos[produtoAtual].peso + 50 <= 2000)
                SelecaoProdutos.listaDeProdutos[produtoAtual].peso += 50

            val stringAtual: String = String.format("%d g", SelecaoProdutos.listaDeProdutos[produtoAtual].peso)

            tvPesoAtual.setText(stringAtual)

        }

        botaoMenos50.setOnClickListener{
            if(SelecaoProdutos.listaDeProdutos[produtoAtual].peso - 50 >= 0)
                SelecaoProdutos.listaDeProdutos[produtoAtual].peso -= 50

            val stringAtual: String = String.format("%d g", SelecaoProdutos.listaDeProdutos[produtoAtual].peso)

            tvPesoAtual.setText(stringAtual)

        }

        botaoConfirmarPeso.setOnClickListener{
            /* Pensando se vale a pena fazer isso, qqr coisa a gente verifica na montagem do pacote bluetooth */
            //if(SelecaoProdutos.listaDeProdutos[produtoAtual].peso == 0)
            //   SelecaoProdutos.listaDeProdutos[produtoAtual].escolhido = false

            do
            {
                produtoAtual++

                if(produtoAtual >= 3)
                    break
            } while(!SelecaoProdutos.listaDeProdutos[produtoAtual].escolhido)

            if(produtoAtual < 3 && SelecaoProdutos.listaDeProdutos[produtoAtual].escolhido){
                val stringAtual: String = String.format("%d g", SelecaoProdutos.listaDeProdutos[produtoAtual].peso)

                tvItemAtual.text = SelecaoProdutos.listaDeProdutos[produtoAtual].nome
                tvPesoAtual.setText(stringAtual)
            }
            else
            {
                /* Troca de tela para confirmação de compra */
                val intent = Intent(this@SelecaoPesos, ConfirmacaoCompra::class.java)

                startActivity(intent)
            }


        }
    }

    override fun onResume() {
        super.onResume()
        setContentView(R.layout.activity_selecao_pesos)

        val botaoMais50 = findViewById<Button>(R.id.bMais50g)
        val botaoMenos50 = findViewById<Button>(R.id.bMenos50g)
        val botaoConfirmarPeso = findViewById<Button>(R.id.bConfirmarPeso)

        val tvItemAtual = findViewById<TextView>(R.id.tvItemAtual)
        val tvPesoAtual = findViewById<TextView>(R.id.tvPesoAtual)

        var produtoAtual = 0

        while(!SelecaoProdutos.listaDeProdutos[produtoAtual].escolhido)
        {
            produtoAtual++

            if(produtoAtual >= 3)
            {
                /* Nenhum produto escolhido? */
                //TODO: tratar erro

                val intent = Intent(this@SelecaoPesos, ConfirmacaoCompra::class.java)

                startActivity(intent)
            }
        }

        tvPesoAtual.setText(String.format("%d g",
            SelecaoProdutos.listaDeProdutos[produtoAtual].peso))
        tvItemAtual.text = SelecaoProdutos.listaDeProdutos[produtoAtual].nome

        botaoMais50.setOnClickListener{
            if(SelecaoProdutos.listaDeProdutos[produtoAtual].peso + 50 <= 2000)
                SelecaoProdutos.listaDeProdutos[produtoAtual].peso += 50

            val stringAtual: String = String.format("%d g", SelecaoProdutos.listaDeProdutos[produtoAtual].peso)

            tvPesoAtual.setText(stringAtual)

        }

        botaoMenos50.setOnClickListener{
            if(SelecaoProdutos.listaDeProdutos[produtoAtual].peso - 50 >= 0)
                SelecaoProdutos.listaDeProdutos[produtoAtual].peso -= 50

            val stringAtual: String = String.format("%d g", SelecaoProdutos.listaDeProdutos[produtoAtual].peso)

            tvPesoAtual.setText(stringAtual)

        }

        botaoConfirmarPeso.setOnClickListener{
            /* Pensando se vale a pena fazer isso, qqr coisa a gente verifica na montagem do pacote bluetooth */
            //if(SelecaoProdutos.listaDeProdutos[produtoAtual].peso == 0)
            //   SelecaoProdutos.listaDeProdutos[produtoAtual].escolhido = false

            do
            {
                produtoAtual++

                if(produtoAtual >= 3)
                    break
            } while(!SelecaoProdutos.listaDeProdutos[produtoAtual].escolhido)

            if(produtoAtual < 3 && SelecaoProdutos.listaDeProdutos[produtoAtual].escolhido){
                val stringAtual: String = String.format("%d g", SelecaoProdutos.listaDeProdutos[produtoAtual].peso)

                tvItemAtual.text = SelecaoProdutos.listaDeProdutos[produtoAtual].nome
                tvPesoAtual.setText(stringAtual)
            }
            else
            {
                /* Troca de tela para confirmação de compra */
                val intent = Intent(this@SelecaoPesos, ConfirmacaoCompra::class.java)

                startActivity(intent)
            }


        }
    }
}