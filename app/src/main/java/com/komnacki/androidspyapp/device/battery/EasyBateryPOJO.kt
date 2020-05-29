package com.komnacki.androidspyapp.device.battery

import com.google.gson.annotations.SerializedName
import com.komnacki.androidspyapp.POJO

class EasyBatteryPOJO : POJO {
    constructor()
    constructor(
        value : Int,
        isCharge : Boolean,
        batteryTechnology : String,
        temperature : Float,
        voltage : Int,
        hasBattery : Boolean
    ) {
        this.value = value
        this.isCharge = isCharge
        this.batteryTechnology = batteryTechnology
        this.temperature = temperature
        this.voltage = voltage
        this.hasBattery = hasBattery
    }

    @SerializedName("value")
    var value : Int? = null

    @SerializedName("isCharge")
    var isCharge : Boolean? = null

    @SerializedName("batteryTechnology")
    var batteryTechnology : String? = null

    @SerializedName("temperature")
    var temperature : Float? = null

    @SerializedName("voltage")
    var voltage : Int? = null

    @SerializedName("hasBattery")
    var hasBattery : Boolean? = null

    override fun toString() : String {
        return "Battery: value: $value" +
                " isCharge: $isCharge" +
                " batteryTechnology: $batteryTechnology" +
                " temperature: $temperature" +
                " voltage: $voltage" +
                " hasBattery: $hasBattery"
    }
}