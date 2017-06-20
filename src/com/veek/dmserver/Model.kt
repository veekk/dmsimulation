package com.veek.dmserver


/**
 * Created by veek on 14.06.17.
 */

data class Model(var isEnabled: Boolean = false, var isRandom: Boolean = false, var type: SensorType, var value : Int = 0)