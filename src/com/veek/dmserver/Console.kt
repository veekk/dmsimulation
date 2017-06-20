package com.veek.dmserver

import javafx.application.Platform
import javafx.scene.control.IndexRange
import javafx.scene.control.TextArea
import java.io.IOException
import java.io.OutputStream

class Console(private val console: TextArea) : OutputStream() {

    fun appendText(valueOf: String) {
        Platform.runLater({ console.appendText(valueOf) })
    }

    @Throws(IOException::class)
    override fun write(b: Int) {
        appendText(b.toChar().toString())
    }
}