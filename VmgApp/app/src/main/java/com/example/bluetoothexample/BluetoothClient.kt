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
    val taglog = "VMGBluetoothClient"
    var deviceVMG : BluetoothDevice? = null
    var started : Boolean = false
    var mmBuffer: ByteArray = ByteArray(1024) // mmBuffer store for the stream
    var mmSocket : BluetoothSocket? = mmSocket
    var adapter : BluetoothAdapter? = adapter
    var paired = false
    var sent = false
    var currentPosBuffer : Int = 0
    var atual = 0
    var filaMsgs : Vector<ByteArray> = Vector(10)

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
        connectThread().start()
        bluetoothThread().start()
        writeThread().start()
    }


    fun write(bytes : ByteArray)
    {
        bluetoothThread().write(bytes)
    }

    private inner class writeThread() : Thread() {
        override fun run()
        {
            var writing : Boolean = false
            while(true)
            {
                if(TelaConexao.conectado)
                {
                    if(mmSocket != null && paired && TelaConexao.conectado && started) {
                        if (mmSocket!!.outputStream != null && TelaConexao.conectado) {
                            try {
                                if(filaMsgs.size >= 1){
                                    writing = true
                                    mmSocket!!.outputStream.write(filaMsgs[0])
                                    filaMsgs.removeElementAt(0)
                                    atual--
                                    writing = false
                                }
                            } catch (e: IOException) {
                                Log.e(ContentValues.TAG, "Error occurred when sending data", e)
                                writing = false
                                paired = false
                                TelaConexao.conectado = false
                            }
                        }
                    }
                }
            }
        }
    }

    private inner class connectThread() : Thread()
    {
        @SuppressLint("MissingPermission")
        override fun run() {
            super.run()
            /* Enquanto não estiver pareado, */
            while (!TelaConexao.conectado) {
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
                }
                if(!TelaConexao.conectado && paired && deviceVMG != null) {
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
                            TelaConexao.conectado = true
                            paired = true

                            // The connection attempt succeeded. Perform work associated with
                            // the connection.
                            mmBuffer = ByteArray(1024) // mmBuffer store for the stream
                            sendAck()
                        }

                    } catch (e: IOException) {
                        try {
                            mmSocket!!.close()
                            paired = false
                            TelaConexao.conectado = false
                        } catch (e2: IOException) {
                        }
                    }
                }
            }
        }
    }
    fun ByteArray.toHex(): String = joinToString(separator = "") { eachByte -> "%02x".format(eachByte) }

    private inner class bluetoothThread() : Thread()
    {
        @SuppressLint("MissingPermission")
        @RequiresApi(Build.VERSION_CODES.M)
        override fun run() {
            /* Pega um adapter */
            super.run()
            started = true

            if (adapter == null)
                return

            while(true) {

                if(mmSocket != null && TelaConexao.conectado && started && paired) {
                    // Keep listening to the InputStream until an exception occurs
                    // Read from the InputStream.
                    var inStream: InputStream = mmSocket!!.inputStream
                    try {
                        currentPosBuffer += inStream.read(mmBuffer, currentPosBuffer, 1)

                    }
                    catch (e: IOException)
                    {
                        currentPosBuffer = 0
                        paired = false
                        TelaConexao.conectado = false
                    }
                    //implementa leitura de pacote recebido da esp32
                    if (currentPosBuffer >= 4) {
                        Log.d(taglog, "%02x".format(mmBuffer[currentPosBuffer-4]))
                        Log.d(taglog, "%02x".format(mmBuffer[currentPosBuffer-3]))
                        Log.d(taglog, "%02x".format(mmBuffer[currentPosBuffer-2]))
                        Log.d(taglog, "%02x".format(mmBuffer[currentPosBuffer-1]))
                        if (mmBuffer[currentPosBuffer - 4] == 0x12.toByte() &&
                            mmBuffer[currentPosBuffer - 3] == 0x34.toByte() &&
                            mmBuffer[currentPosBuffer - 2] == 0x12.toByte() &&
                            mmBuffer[currentPosBuffer - 1] == 0x34.toByte()
                        ) {
                            //Pacote novo recebido - ler novos dados
                            currentPosBuffer = 0;
                        }
                        //Terminou de receber um pacote
                        else if (mmBuffer[currentPosBuffer - 4] == 0xFF.toByte() &&
                            mmBuffer[currentPosBuffer - 3] == 0xFF.toByte() &&
                            mmBuffer[currentPosBuffer - 2] == 0xFF.toByte() &&
                            mmBuffer[currentPosBuffer - 1] == 0xFF.toByte()
                        ) {
                            //Pacote de adicao de creditos
                            if (mmBuffer[0] == 5.toByte()) {
                                Log.d(taglog, "Pacote de adição de créditos recebido")
                                TelaConexao.usuario.creditos =
                                    (mmBuffer[7] * 256 * 256 + mmBuffer[8] * 256 + mmBuffer[9])
                            }

                            currentPosBuffer = 0;
                        }
                    }
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
            if(TelaConexao.conectado)
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
        //semaforo
        var writing : Boolean = false
        fun write(bytes: ByteArray)
        {
            filaMsgs.add(bytes)
            atual++
            if(!started)
                start()
        }
    }
}