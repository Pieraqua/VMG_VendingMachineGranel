package com.example.bluetoothexample

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.util.*

class TelaConexao : AppCompatActivity() {
    companion object TelaConexao{
        var usuario : Usuario = Usuario(0, 0)
        var conectado : Boolean = false
        var bluetoothClient = BluetoothClient(null,null)
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_conexao)

        if(bluetoothClient.adapter == null) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED -> {
                    // You can directly ask for the permission.
                    // The registered ActivityResultCallback gets the result of this request.
                    requestPermissions(
                        arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                        1
                    )
                }
            }

            val bluetoothManager: BluetoothManager = ContextCompat.getSystemService(
                this,
                BluetoothManager::class.java
            )!!
            val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.getAdapter()
            if (bluetoothAdapter == null) {
                // Device doesn't support Bluetooth
                val intent = Intent(this, com.example.bluetoothexample.TelaConexao::class.java)

                startActivity(intent)
            }

            if (bluetoothAdapter?.isEnabled == false) {
                val intent = Intent(this, com.example.bluetoothexample.TelaConexao::class.java)

                startActivity(intent)
            }

            com.example.bluetoothexample.TelaConexao.bluetoothClient.adapter = bluetoothAdapter
        }
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

        if(!bluetoothClient.started)
            bluetoothClient.start()
        else
        {
            bluetoothClient.sendAck()
        }

    }

    override fun onResume() {
        super.onResume()

        if(!bluetoothClient.started)
            bluetoothClient.start()
        else
        {
            bluetoothClient.sendAck()
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