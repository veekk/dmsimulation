package com.veek.dmserver

import java.io.*

import javax.bluetooth.RemoteDevice
import javax.microedition.io.Connector
import javax.microedition.io.StreamConnectionNotifier
import java.util.Random
import javax.bluetooth.UUID
import javax.microedition.io.StreamConnection

/**
 * Class that implements an SPP Server which accepts single line of
 * message from an SPP client and sends a single line of response to the client.
 */
class SimpleSPPServer {

    var isRunning = false

    lateinit var connection: StreamConnection

    lateinit var dev: RemoteDevice
    lateinit var bReader: BufferedReader
    lateinit var pWriter: PrintWriter

    lateinit var inStream: InputStream

    lateinit var streamConnNotifier: StreamConnectionNotifier

    val random = Random()

    init {

    }

    fun startServer() {

        val uuid = UUID("1101", true)
        val connectionString = "btspp://localhost:$uuid;name=DII"
        streamConnNotifier = Connector.open(connectionString) as StreamConnectionNotifier
        connection = streamConnNotifier.acceptAndOpen()
        dev = RemoteDevice.getRemoteDevice(connection)
        inStream = connection.openInputStream()
        bReader = BufferedReader(InputStreamReader(inStream))

        streamConnNotifier.close()

        val outStream = connection.openOutputStream()
        pWriter = PrintWriter(OutputStreamWriter(outStream))

        isRunning = true


        println("\nServer Started. Waiting for clients to connect...")





        println("Remote device address: " + dev.bluetoothAddress)
        println("trying to send data")



        Thread(Runnable {
            while (isRunning) {
                try {
                    Thread.sleep(50)
                    val lineRead = inStream.read()
                    println("device said:" + lineRead)
                } catch (ignored: IOException) {
                }

            }
        }).start()

        while (isRunning) {
            try {
                Thread.sleep((1000 / Main.frequency).toLong())
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            var message = ""

            Main.list.forEach {
                if (it.isEnabled || Main.isGlobalEnabled) {
                    var value = if (it.isRandom || Main.isGlobalRandom) random.nextInt(it.type.maxValue) else it.value
                    message += "${it.type.key}:$value "
                }
            }

            if (message.isNotBlank() && message.isNotEmpty()) {
                pWriter.write("$message \r")
                 println(message)
                pWriter.flush()
            }
        }

    }

    fun stopServer() {
        isRunning = false
        println("Sending stopped")
    }

}


