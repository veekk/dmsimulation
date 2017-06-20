package com.veek.dmserver

/**
 * Created by veek on 13.06.17.
 */

import javafx.application.Application
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader.load
import javafx.geometry.Insets
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.io.IOException
import java.io.PrintStream
import java.util.concurrent.Executors
import kotlin.concurrent.thread

class Main : Application() {


    private var layout = "/main.fxml"

    @FXML lateinit var start: Button
    @FXML lateinit var stop: Button
    @FXML lateinit var reset: Button
    @FXML lateinit var console: TextArea
    @FXML lateinit var container: VBox
    @FXML lateinit var randomCb: CheckBox
    @FXML lateinit var enableCb: CheckBox
    @FXML lateinit var freq: Slider
    @FXML lateinit var freqL: Label

    var sps : SimpleSPPServer? = null
    var thread = Executors.newSingleThreadExecutor()


    override fun start(primaryStage: Stage) {
        primaryStage.scene = Scene(load<Parent?>(javaClass.getResource(layout)))
        primaryStage.show()


    }

    companion object {

        var list = listOf<Model>()
        var isGlobalRandom = false
        var isGlobalEnabled = false
        var frequency = 20

        var alert = Alert(Alert.AlertType.ERROR).apply {
            contentText = "Check bluetooth"
        }

        @JvmStatic fun main(args: Array<String>) {

            Application.launch(Main::class.java)

        }

    }

    @FXML @Throws(IOException::class) fun start(e: ActionEvent) {
        sps = SimpleSPPServer()
        thread.shutdown()
        thread = Executors.newSingleThreadExecutor()
        thread.submit { sps?.startServer() }
    }

    @FXML fun stop(e: ActionEvent) {
        sps?.stopServer()
        thread.shutdown()
    }

    @FXML fun reset(e: ActionEvent) {
    //    sps = SimpleSPPServer()
    }

    fun initialize() {
        val ps = PrintStream(Console(console))
        System.setOut(ps)
        randomCb.selectedProperty().addListener { _, _, newValue ->
            isGlobalRandom = newValue
        }

        enableCb.selectedProperty().addListener { _, _, newValue ->
            isGlobalEnabled = newValue

        }

        freq.valueProperty().addListener { _, _, newValue ->
            frequency = newValue.toInt()
            freqL.text = frequency.toString()
        }

        freq.minProperty().value = 10.0
        freq.maxProperty().value = 200.0

        list = listOf()
        SensorType.values().forEach {
            list += Model(type = it)
        }
        list.forEach {
            container.children.add(HBox().apply {
                with(children) {
                    add(CheckBox().apply {
                        selectedProperty().addListener { _, _, newValue ->
                            it.isEnabled = newValue
                        }

                        text = it.type.namee
                        minWidth = 100.0
                    })
                    add(ScrollBar().apply {
                        valueProperty().addListener { _, _, newValue ->
                            it.value = newValue.toInt()
                        }
                        minWidth = 120.0
                        padding = Insets(0.0, 5.0, 0.0, 0.0)
                        max = it.type.maxValue.toDouble()
                    })
                    add(CheckBox().apply {
                        text = "Random"
                        selectedProperty().addListener { _, _, newValue ->
                            it.isRandom = newValue
                        }
                    })
                }
            })
        }
    }

    override fun stop() {
        sps?.stopServer()
        thread.shutdown()
        super.stop()
    }

}
