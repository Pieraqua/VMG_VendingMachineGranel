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
    mmSocket: BluetoothSocket?
) {
    var started : Boolean = false
    var mmBuffer: ByteArray = ByteArray(1024) // mmBuffer store for the stream
    var mmSocket : BluetoothSocket? = mmSocket
    var adapter : BluetoothAdapter? = adapter
    var paired = false
    var sent = false
    var currentPosBuffer : Int = 0

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
            started = true

            if (adapter == null)
                return

            while(true) {
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
                                deviceVMG!!.createInsecureRfcommSocketToServiceRecord(
                                    UUID.fromString(
                                        "00001101-0000-1000-8000-00805F9B34FB"
                                    )
                                )
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
                                sendAck()
                            }

                        } catch (e: IOException) {
                            try {
                                mmSocket!!.close()
                                paired = false
                                connected = false
                                cancel()
                            } catch (e2: IOException) {
                                cancel()
                            }
                        }
                    }
                }
                if(mmSocket != null) {
                    // Keep listening to the InputStream until an exception occurs
                    // Read from the InputStream.
                    var inStream: InputStream = mmSocket!!.inputStream
                    currentPosBuffer += inStream.read(mmBuffer, currentPosBuffer, 1)

                    //implementa leitura de pacote recebido da esp32
                    if (currentPosBuffer >= 4) {

                        if (mmBuffer[currentPosBuffer - 3] == 0x12.toByte() &&
                            mmBuffer[currentPosBuffer - 2] == 0x34.toByte() &&
                            mmBuffer[currentPosBuffer - 1] == 0x12.toByte() &&
                            mmBuffer[currentPosBuffer] == 0x34.toByte()
                        ) {
                            //Pacote novo recebido - ler novos dados
                            currentPosBuffer = 0;
                        }
                        //Terminou de receber um pacote
                        if (mmBuffer[currentPosBuffer - 3] == 0xFF.toByte() &&
                            mmBuffer[currentPosBuffer - 2] == 0xFF.toByte() &&
                            mmBuffer[currentPosBuffer - 1] == 0xFF.toByte() &&
                            mmBuffer[currentPosBuffer] == 0xFF.toByte()
                        ) {
                            //Pacote de adicao de creditos
                            if (mmBuffer[0] == 5.toByte()) {
                                TelaConexao.usuario.creditos =
                                    (mmBuffer[7] * 256 * 256 + mmBuffer[8] * 256 + mmBuffer[9])
                            }

                            currentPosBuffer = 0;
                        }
                    }
                }
                else
                {
                    cancel()
                }
            }
        }
        private fun criaPacoteCredito(dinheiros : Int) : ByteArray
        {
            var pacote = ByteArray(15)

            pacote[0] = 0x12.toByte()
            pacote[1] = 0x34.toByte()
            pacote[2] = 0x12.toByte()
            pacote[3] = 0x34.toByte()

            pacote[4] = 0x01;
            pacote[5] = dinheiros.toByte();
            /* Implementar usuários */
            pacote[6] = 0x11;
            pacote[7] = 0x11;
            pacote[8] = 0x11;
            pacote[9] = 0x11;

            var checksum = 0;

            for(x in pacote)
            {
                checksum += x;
            }

            pacote[10] = (checksum%256).toByte();

            pacote[11] = 0xFF.toByte()
            pacote[12] = 0xFF.toByte()
            pacote[13] = 0xFF.toByte()
            pacote[14] = 0xFF.toByte()

            return pacote
        }

        private fun criaPacotePedido() :ByteArray
        {
            var pacote = ByteArray(16)
            var peso = 0
            var checksum = 2

            pacote[0] = 0x12.toByte()
            pacote[1] = 0x34.toByte()
            pacote[2] = 0x12.toByte()
            pacote[3] = 0x34.toByte()

            pacote[4] = 0x02
            var i = 5

            for(x in 0..2) {
                peso = SelecaoProdutos.listaDeProdutos[x].peso
                pacote[i++] = (peso / 256).toByte()
                pacote[i++] = (peso).toByte()
                checksum += peso
            }

            pacote[i++] = (checksum%256).toByte()

            pacote[i++] = 0xFF.toByte()
            pacote[i++] = 0xFF.toByte()
            pacote[i++] = 0xFF.toByte()
            pacote[i++] = 0xFF.toByte()

            return pacote
        }

        private fun criaAck() : ByteArray
        {
            var pacote = ByteArray(14)
            var checksum = 0

            pacote[0] = 0x12.toByte()
            pacote[1] = 0x34.toByte()
            pacote[2] = 0x12.toByte()
            pacote[3] = 0x34.toByte()

            pacote[4] = 0x03

            pacote[5] = 0x01
            pacote[6] = 0x23
            pacote[7] = 0x45
            pacote[8] = 0x67

            for (x in pacote){
                checksum += x
            }

            pacote[9] = checksum.toByte()

            pacote[10] = 0xFF.toByte()
            pacote[11] = 0xFF.toByte()
            pacote[12] = 0xFF.toByte()
            pacote[13] = 0xFF.toByte()

            return pacote
        }

        private fun criaAckRequest() : ByteArray
        {
            var pacote = ByteArray(14)

            pacote[0] = 0x12
            pacote[1] = 0x34
            pacote[2] = 0x12
            pacote[3] = 0x34

            pacote[4] = 0x04
            pacote[5] = 0x76
            pacote[6] = 0x54
            pacote[7] = 0x32
            pacote[8] = 0x10

            var checksum = 0

            for(x in pacote)
            {
                checksum += x
            }

            pacote[9] = checksum.toByte()

            pacote[10] = 0xFF.toByte()
            pacote[11] = 0xFF.toByte()
            pacote[12] = 0xFF.toByte()
            pacote[13] = 0xFF.toByte()


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
                    cancel()
                }
            }
            else
            {
            }
        }


        // Closes the client socket and causes the thread to finish.
        fun cancel() {
            try {
                currentPosBuffer = 0
                started = false
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