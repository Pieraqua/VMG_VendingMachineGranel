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


class BluetoothClient() {
    var mmBuffer: ByteArray? = null//= ByteArray(1024) // mmBuffer store for the stream
    var mmSocket : BluetoothSocket? = null
    var adapter : BluetoothAdapter? = null

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
        try {
            mmSocket!!.outputStream.write(bytes)
        } catch (e: IOException) {
            Log.e(ContentValues.TAG, "Error occurred when sending data", e)
        }
    }

    private inner class bluetoothThread() : Thread()
    {
        var paired = false
        var connected = false
        var deviceVMG : BluetoothDevice? = null

        @SuppressLint("MissingPermission")
        @RequiresApi(Build.VERSION_CODES.M)
        override fun run() {
            /* Pega um adapter */
            super.run()

            while(adapter == null)
            {
                sleep(500)
            }
            /* Enquanto não estiver pareado, */
            while(!paired || !connected ) {
                val pairedDevices: Set<BluetoothDevice>? = adapter?.bondedDevices
                pairedDevices?.forEach { device ->
                    val deviceName = device.name
                    val deviceHardwareAddress = device.address // MAC address

                    if (deviceName == "VMGaGranel") {
                        paired = true
                        deviceVMG = device
                    }


                    if(!paired)
                    {
                        sleep(500)
                    }

                    try {
                        mmSocket = deviceVMG!!.createInsecureRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"))
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

                    }
                    catch (e: IOException) {
                        try {
                            mmSocket!!.close()
                        } catch (e2: IOException) {

                        }
                    }
                }
            }
        }

        // Closes the client socket and causes the thread to finish.
        fun cancel() {
            try {
                connected = false
                TelaConexao.conectado = false
                mmSocket?.close()
            } catch (e: IOException) {
                Log.e(TAG, "Could not close the client socket", e)
            }
        }
    }
}