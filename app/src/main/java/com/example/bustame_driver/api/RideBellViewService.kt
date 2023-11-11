package com.example.bustame_driver.api

import com.example.bustame_driver.model.RideBellBody
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface RideBellViewService {

    @GET("/RideBell/BusNum/{busNumber")
    fun findRideBell(@Path("busNumber") busNumber: String):  Call<ArrayList<RideBellBody>>

    @DELETE("/RideBell")
    fun deleteAllRideBell(): Call<Unit>
}