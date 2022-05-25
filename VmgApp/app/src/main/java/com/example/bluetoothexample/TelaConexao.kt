package com.example.bluetoothexample

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.app.ActivityCompat
import java.io.IOException
import java.util.*

class TelaConexao : AppCompatActivity() {
    companion object TelaConexao{
        val usuario : Usuario = Usuario(0.0F, 0)

    }
    var mmSocket: BluetoothSocket? = null
    var device : BluetoothDevice? = null
    var paired : Boolean = false
    // Create a BroadcastReceiver for ACTION_FOUND.
    private val receiver = object : BroadcastReceiver() {

        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent) {
            val action: String? = intent.action
            when(action) {
                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val deviceb: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val deviceName = deviceb?.name
                    val deviceHardwareAddress = deviceb?.address // MAC address

                    if(deviceName == "VMGaGranel")
                    {
                        device = deviceb
                        paired = true
                    }
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_conexao)


        val buttonConfirmarConexao:Button = findViewById(R.id.confirmConnect)
        buttonConfirmarConexao.setOnClickListener{
            val intent = Intent(this@TelaConexao, SelecaoProdutos::class.java)
            resetListaCompras()

            val threadConexao = ConnectThread()
            threadConexao.start()

            startActivity(intent)
        }

        val buttonAdicaoCreditos = findViewById<Button>(R.id.botaoAdicionarCreditos)
        buttonAdicaoCreditos.setOnClickListener{
            val intent = Intent(this@TelaConexao, AdicaoCreditos::class.java)
            startActivity(intent)
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

    @SuppressLint("MissingPermission")
    private inner class ConnectThread() : Thread() {


        private val MY_UUID : UUID = UUID(0x12345678, 0x12345678)


        override fun run() {
            /* Pega adaptador bluetooth e permissões */
            val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
            if (bluetoothAdapter == null) {
                // Device doesn't support Bluetooth
            }

            if (bluetoothAdapter?.isEnabled == false) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                if (ActivityCompat.checkSelfPermission(
                        this@TelaConexao,
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

            val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
            pairedDevices?.forEach { devicepaired ->
                val deviceName = devicepaired.name
                val deviceHardwareAddress = devicepaired.address // MAC address

                if(deviceName == "VMGaGranel")
                {
                    paired = true
                    device = devicepaired
                }
            }

            if(!paired)
            {
                /* TODO: Implement scan */
                // Register for broadcasts when a device is discovered.
                /* Bluetooth */
                val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
                registerReceiver(receiver, filter)

                while(!paired)
                {}

                unregisterReceiver(receiver)
            }

            mmSocket = device?.createRfcommSocketToServiceRecord(MY_UUID)



            mmSocket?.let { socket ->
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                socket.connect()

                // The connection attempt succeeded. Perform work associated with
                // the connection in a separate thread.
                manageMyConnectedSocket(socket)
            }
        }

        // Closes the client socket and causes the thread to finish.
        fun cancel() {
            try {
                mmSocket?.close()
                unregisterReceiver(receiver)
            } catch (e: IOException) {
                Log.e(TAG, "Could not close the client socket", e)
            }
        }
    }

    private inner class manageMyConnectedSocket(socket: BluetoothSocket) : Thread()
    {
        val bsocket = socket
        public override fun run()
        {

        }

        fun quit()
        {
            bsocket.close()
        }
    }
}