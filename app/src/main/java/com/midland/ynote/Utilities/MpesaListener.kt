package com.midland.ynote.Utilities

interface MpesaListener {
    fun sendingSuccessful(transactionAmount: String, phoneNumber: String, transactionDate: String, MPesaReceiptNo: String)

    fun sendingFailed(cause: String)
}