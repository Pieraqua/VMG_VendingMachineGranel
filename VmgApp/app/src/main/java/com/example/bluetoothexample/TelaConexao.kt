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
    }

    inner class BluetoothClient(){
        var bcontext = this@TelaConexao

        fun start()
        {
            bluetoothThread().start()
        }

        fun cancel()
        {
            bluetoothThread().cancel()
        }

        fun write(bytes: ByteArray)
        {
            bluetoothThread().write(bytes)
        }

        private inner class bluetoothThread() : Thread()
        {
            var paired = false
            var connected = false
            var deviceVMG : BluetoothDevice? = null
            var mmSocket : BluetoothSocket? = null
            var mmInStream: InputStream?  = null//= mmSocket!!.inputStream
            var mmOutStream: OutputStream? = null//= mmSocket!!.outputStream
            var mmBuffer: ByteArray? = null//= ByteArray(1024) // mmBuffer store for the stream

            @RequiresApi(Build.VERSION_CODES.M)
            override fun run() {
                /* Pega um adapter */
                super.run()
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
                        this@TelaConexao,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED -> {
                        // You can directly ask for the permission.
                        // The registered ActivityResultCallback gets the result of this request.
                        requestPermissions(
                            arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                            1)
                    }
                }
                /* Enquanto não estiver pareado, */
                while(!paired) {
                    val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
                    pairedDevices?.forEach { device ->
                        val deviceName = device.name
                        val deviceHardwareAddress = device.address // MAC address

                        if (deviceName == "VMGaGranel") {
                            paired = true
                            deviceVMG = device
                        }
                    }

                    if(!paired)
                    {
                        Thread.sleep(500)
                    }
                }
                while(!connected) {
                    try {
                        mmSocket = deviceVMG!!.createRfcommSocketToServiceRecord(UUID.randomUUID())
                        mmSocket?.let { socket ->
                            // Connect to the remote device through the socket. This call blocks
                            // until it succeeds or throws an exception.
                            socket.connect()
                            connected = true
                            conectado = true

                            // The connection attempt succeeded. Perform work associated with
                            // the connection.

                            mmInStream = mmSocket!!.inputStream
                            mmOutStream = mmSocket!!.outputStream
                            mmBuffer = ByteArray(1024) // mmBuffer store for the stream

                        }

                    }
                    catch (e: IOException) {
                        try {
                            mmSocket!!.close()
                        } catch (e2: IOException) {

                        }
                    }
                }

            }
            // Call this from the main activity to send data to the remote device.
            fun write(bytes: ByteArray) {
                if(mmOutStream == null)
                {
                    return
                }
                try {
                    mmOutStream!!.write(bytes)
                } catch (e: IOException) {
                    Log.e(TAG, "Error occurred when sending data", e)
                    return
                }
            }

            // Closes the client socket and causes the thread to finish.
            fun cancel() {
                try {
                    mmSocket?.close()
                    connected = false
                    conectado = false
                } catch (e: IOException) {
                    Log.e(TAG, "Could not close the client socket", e)
                }
            }
        }
    }

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

        BluetoothClient().start()
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