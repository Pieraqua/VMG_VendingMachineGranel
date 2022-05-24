package com.example.bluetoothexample

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.ActivityCompat

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

        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
        }

        if (bluetoothAdapter?.isEnabled == false) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            startActivityForResult(enableBtIntent, 1)
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