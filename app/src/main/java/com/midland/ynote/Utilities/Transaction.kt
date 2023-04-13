package com.midland.ynote.Utilities

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body

data class Transaction( @SerializedName("Body")
                        val body: Body
) {
    data class Body(
        @SerializedName("stkCallback")
        val stkCallback: StkCallback
    ){

        data class StkCallback(
            @SerializedName("CallbackMetadata")
            val callbackMetadata: CallbackMetadata,
            @SerializedName("CheckoutRequestID")
            val checkoutRequestID: String,
            @SerializedName("MerchantRequestID")
            val merchantRequestID: String,
            @SerializedName("ResultCode")
            val resultCode: Int,
            @SerializedName("ResultDesc")
            val resultDesc: String
        ) {
            data class CallbackMetadata(
                @SerializedName("Item")
                val item: List<Item>
            ) {
                data class Item(
                    @SerializedName("Name")
                    val name: String,
                    @SerializedName("Value")
                    val value: String
                )
            }
        }
    }

}