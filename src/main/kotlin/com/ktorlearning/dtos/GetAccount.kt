package com.ktorlearning.dtos

import com.ktorlearning.models.Balance
import kotlinx.serialization.Serializable

@Serializable
data class GetAccount(val id: String, val name: String, val balances: Collection<Balance>)