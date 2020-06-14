//package com.komnacki.androidspyapp.device.battery
//
//import android.content.Context
//import com.komnacki.androidspyapp.api.ApiResponse
//import com.komnacki.androidspyapp.api.PermissionsService
package com.komnacki.androidspyapp.device.battery

//import github.nisrulz.easydeviceinfo.base.EasyBatteryMod
//import io.reactivex.rxjava3.core.Observable
//
//class EasyBattery : PojoFeeder {
//    constructor(context : Context) {
//        this.easyBatteryMod = EasyBatteryMod(context)
//    }
//
//    var easyBatteryMod : EasyBatteryMod? = null
//
//    override fun getPOJO() : EasyBatteryPOJO {
//        return EasyBatteryPOJO()
//    }
//
//    override fun sendPOJO(service : PermissionsService, email : String) : Observable<ApiResponse> {
//        return service.sendBatteryState(email, getBatteryState())
//    }
//
//    private fun getBatteryState() : EasyBatteryPOJO {
//        val value = easyBatteryMod !!.batteryPercentage
//        val isCharge = easyBatteryMod !!.isDeviceCharging
//        val technology = easyBatteryMod !!.batteryTechnology
//        val temperature = easyBatteryMod !!.batteryTemperature
//        val voltage = easyBatteryMod !!.batteryVoltage
//        val hasBattery = easyBatteryMod !!.isBatteryPresent
//        return EasyBatteryPOJO(value, isCharge, technology, temperature, voltage, hasBattery)
//    }
//
//}