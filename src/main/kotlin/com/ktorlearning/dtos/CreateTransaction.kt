package com.ktorlearning.dtos

data class CreateTransaction(
    val sourceAccountId: String,
    val destinationAccountId: String,
    val value: Double,
    val balanceType: String
)