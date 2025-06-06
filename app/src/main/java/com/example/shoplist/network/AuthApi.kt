package com.example.shoplist.network

import com.example.shoplist.data.LoginRequest
import com.example.shoplist.data.LoginResponse
import com.example.shoplist.data.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {
    @POST("/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<Void>

    //@GET("/items")
   // suspend fun getCoffees(): List<Coffee>

}
