package com.example.shoplist.network

import ShoppingItem
import com.example.shoplist.data.AddItemRequest
import com.example.shoplist.data.UpdateItemRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CrudApi {
    @GET("/items")
    suspend fun getItems(): Response<List<ShoppingItem>>

    @POST("/items/")
    suspend fun addItem(@Body request: AddItemRequest): Response<ShoppingItem>

    @PUT("/items/{id}")
    suspend fun updateItem(@Path("id") id: Int, @Body updated: UpdateItemRequest): Response<ShoppingItem>

    @DELETE("/items/{id}")
    suspend fun deleteItem(@Path("id") id: Int): Response<Void>

}
