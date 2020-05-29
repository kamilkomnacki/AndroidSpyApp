package com.komnacki.androidspyapp

import com.komnacki.androidspyapp.api.ApiResponse
import com.komnacki.androidspyapp.api.PermissionsService
import io.reactivex.rxjava3.core.Observable

interface PojoFeeder {
    fun getPOJO() : POJO
    fun sendPOJO(service : PermissionsService, email : String) : Observable<ApiResponse>
}