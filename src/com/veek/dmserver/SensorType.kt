package com.veek.dmserver

/**
 * Created by veek on 14.06.17.
 */
enum class SensorType(val namee: String, val key: String, val maxValue: Int) {
    BOOST("Boost", "Boost", 200),
    EGT("EGT", "EGT", 2000),
    FUEL_P("Fuel-P", "Fuel-P", 400),
    OIL_P("Oil-P", "Oil-P", 400),
    RPM("RPM", "RPM", 16000),
    SPEED("Speed", "Speed", 400),
    FUEL("Fuel", "Fuel", 2000),
    VOLT("Volt", "Volt", 2000),
    AIR_T("Air-T", "Air-T", 100)
}