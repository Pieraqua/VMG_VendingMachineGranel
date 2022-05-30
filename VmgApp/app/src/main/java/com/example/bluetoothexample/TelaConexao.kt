package com.example.bluetoothexample

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

class TelaConexao : AppCompatActivity() {
    val bcontext = this
    companion object TelaConexao{
        val usuario : Usuario = Usuario(0.0F, 0)
        var conectado : Boolean = false
        var bluetoothClient = BluetoothClient()
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_conexao)

        val buttonConfirmarConexao:Button = findViewById(R.id.confirmConnect)
        buttonConfirmarConexao.setOnClickListener{
            if(conectado) {
                val intent = Intent(this@TelaConexao, SelecaoProdutos::class.java)
                resetListaCompras()

                startActivity(intent)
            }
            else{
                val tvTextoInfo = findViewById<TextView>(R.id.listConexoes)

                tvTextoInfo.text = "Ainda não está conectado!"

            }
        }

        val buttonAdicaoCreditos = findViewById<Button>(R.id.botaoAdicionarCreditos)
        buttonAdicaoCreditos.setOnClickListener{
            if(conectado) {
                val intent = Intent(this@TelaConexao, AdicaoCreditos::class.java)

                startActivity(intent)
            }
            else{
                val tvTextoInfo = findViewById<TextView>(R.id.listConexoes)

                tvTextoInfo.text = "Ainda não está conectado!"
            }

        }

        val bluetoothManager: BluetoothManager = ContextCompat.getSystemService(
            bcontext,
            BluetoothManager::class.java
        )!!
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.getAdapter()
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            return
        }

        if (bluetoothAdapter?.isEnabled == false) {
            return
        }
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissions(
                    arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                    1)
            }
        }

        bluetoothClient.adapter = bluetoothAdapter
        bluetoothClient.start()
    }

    override fun onResume() {
        super.onResume()

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