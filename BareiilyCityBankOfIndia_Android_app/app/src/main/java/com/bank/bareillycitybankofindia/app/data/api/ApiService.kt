package com.bank.bareillycitybankofindia.app.data.api

import com.bank.bareillycitybankofindia.app.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<ApiResponse<String>>

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("mobile") mobile: String,
        @Field("password") password: String,
        @Field("firstName") firstName: String,
        @Field("lastName") lastName: String,
        @Field("dob") dob: String,
        @Field("gender") gender: String,
        @Field("aadhaar") aadhaar: String,
        @Field("pan") pan: String,
        @Field("address") address: String,
        @Field("productId") productId: Int = 1
    ): Response<ApiResponse<String>>

    @GET("customer/accounts")
    suspend fun getAccounts(): Response<ApiResponse<List<Account>>>

    @GET("customer/transactions")
    suspend fun getTransactions(): Response<ApiResponse<List<Transaction>>>

    @FormUrlEncoded
    @POST("customer/transfer")
    suspend fun transferFunds(
        @Field("fromAccountId") fromAccountId: Long,
        @Field("toAccount") toAccount: String,
        @Field("amount") amount: Double,
        @Field("description") description: String
    ): Response<ApiResponse<String>>

    @GET("customer/notifications")
    suspend fun getNotifications(): Response<ApiResponse<List<BankNotification>>>
}
