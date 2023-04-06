package com.ktorlearning.dtos

import kotlinx.serialization.Serializable

@Serializable
data class CreateAccount(val name: String)