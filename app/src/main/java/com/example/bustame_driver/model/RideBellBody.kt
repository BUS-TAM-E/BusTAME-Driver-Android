package com.example.bustame_driver.model

data class RideBellBody(
    //@SerializedName("busNumber")
    val busNumber: String?,
    val busStopNumber: String?,
    val passengerType: String?,
    val message: String?
)
