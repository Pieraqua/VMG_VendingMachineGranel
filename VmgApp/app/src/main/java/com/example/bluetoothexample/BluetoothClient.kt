package com.example.bluetoothexample

import android.Manifest
import android.annotation.SuppressLint
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.security.Provider
import java.util.*
import java.util.logging.Handler
import kotlin.math.exp


class BluetoothClient(
    adapter: BluetoothAdapter?,
    mmSocket: BluetoothSocket?,
    mmBuffer: ByteArray?
) {
    var mmBuffer: ByteArray? = mmBuffer//= ByteArray(1024) // mmBuffer store for the stream
    var mmSocket : BluetoothSocket? = mmSocket
    var adapter : BluetoothAdapter? = adapter
    var paired = false
    var sent = false

    fun sendPedido()
    {
        bluetoothThread().sendPedido()
    }

    fun sendAck()
    {
        bluetoothThread().sendAck()
    }

    fun sendAckRequest()
    {
        bluetoothThread().sendAckRequest()
    }

    fun sendPacoteCredito(dinheiros: Int)
    {
        bluetoothThread().sendPacoteCredito(dinheiros)
    }

    fun start()
    {
        bluetoothThread().start()
    }

    fun cancel()
    {
        bluetoothThread().cancel()
    }

    fun write(bytes : ByteArray)
    {
        bluetoothThread().write(bytes)
    }
    fun getConnected() : Boolean
    {
        return bluetoothThread().connected
    }

    private inner class bluetoothThread() : Thread()
    {

        var connected = false
        var deviceVMG : BluetoothDevice? = null

        @SuppressLint("MissingPermission")
        @RequiresApi(Build.VERSION_CODES.M)
        override fun run() {
            /* Pega um adapter */
            super.run()

            if (adapter == null)
                return

            while(true){
                /* Enquanto não estiver pareado, */
                while (!connected) {
                    while (!paired) {
                        val pairedDevices: Set<BluetoothDevice>? = adapter?.bondedDevices
                        pairedDevices?.forEach { device ->
                            val deviceName = device.name
                            val deviceHardwareAddress = device.address // MAC address

                            if (deviceName == "VMGaGranel") {
                                paired = true
                                deviceVMG = device
                            }


                            if (!paired) {
                                sleep(500)
                            }
                        }

                        try {
                            mmSocket =
                                deviceVMG!!.createInsecureRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"))
                            mmSocket?.let { socket ->
                                // Connect to the remote device through the socket. This call blocks
                                // until it succeeds or throws an exception.
                                sleep(500)
                                socket.connect()
                                connected = true
                                TelaConexao.conectado = true

                                // The connection attempt succeeded. Perform work associated with
                                // the connection.
                                mmBuffer = ByteArray(1024) // mmBuffer store for the stream

                            }

                        } catch (e: IOException) {
                            try {
                                mmSocket!!.close()
                                paired = false
                                connected = false
                            } catch (e2: IOException) {

                            }
                        }
                    }
                }
            }
            sleep(5000)
            write("ping".toByteArray())
        }
        private fun criaPacoteCredito(dinheiros : Int) : ByteArray
        {
            var pacote = ByteArray(7)

            pacote[0] = 0x01;
            pacote[1] = dinheiros.toByte();
            /* Implementar usuários */
            pacote[2] = 0x00;
            pacote[3] = 0x00;
            pacote[4] = 0x00;
            pacote[5] = 0x00;

            var checksum = 0;

            for(x in pacote)
            {
                checksum += x;
            }

            pacote[6] = (checksum%256).toByte();

            return pacote
        }

        private fun criaPacotePedido() :ByteArray
        {
            var pacote = ByteArray(8)
            var peso = 0
            var checksum = 2

            pacote[0] = 0x02
            var i = 1

            for(x in 0..2) {
                peso = SelecaoProdutos.listaDeProdutos[x].peso
                pacote[i++] = (peso / 256).toByte()
                pacote[i++] = (peso).toByte()
                checksum += peso
            }

            pacote[i] = (checksum%256).toByte()

            return pacote
        }

        private fun criaAck() : ByteArray
        {
            var pacote = ByteArray(6)
            var checksum = 0
            pacote[0] = 0x03

            pacote[1] = 0x01
            pacote[2] = 0x23
            pacote[3] = 0x45
            pacote[4] = 0x67

            for (x in pacote){
                checksum += x
            }

            pacote[5] = checksum.toByte()

            return pacote
        }

        private fun criaAckRequest() : ByteArray
        {
            var pacote = ByteArray(6)

            pacote[0] = 0x04
            pacote[1] = 0x76
            pacote[2] = 0x54
            pacote[3] = 0x32
            pacote[4] = 0x10

            var checksum = 0

            for(x in pacote)
            {
                checksum += x
            }

            return pacote
        }

        fun sendPedido()
        {
            write(criaPacotePedido())
        }

        fun sendAck()
        {
            write(criaAck())
        }

        fun sendPacoteCredito(dinheiros: Int)
        {
            write(criaPacoteCredito(dinheiros))
        }

        fun sendAckRequest()
        {
            write(criaAckRequest())
        }

        fun write(bytes: ByteArray)
        {
            if(mmSocket != null && mmSocket!!.outputStream != null) {
                try {
                    mmSocket!!.outputStream.write(bytes)
                    sent = true
                } catch (e: IOException) {
                    Log.e(ContentValues.TAG, "Error occurred when sending data", e)
                    mmSocket!!.close()
                    paired = false
                    connected = false
                }
            }
            else
            {
                paired = false
                connected = false
            }
        }


        // Closes the client socket and causes the thread to finish.
        fun cancel() {
            try {
                paired = false
                connected = false
                TelaConexao.conectado = false
                mmSocket?.close()
            } catch (e: IOException) {
                Log.e(TAG, "Could not close the client socket", e)
            }
        }
    }
}