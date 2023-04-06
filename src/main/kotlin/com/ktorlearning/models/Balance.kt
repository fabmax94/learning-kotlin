package com.ktorlearning.models

import com.ktorlearning.repositories.AccountRepository
import kotlinx.serialization.Serializable


enum class BalanceType {
    FOOD, MEAL, CASH
}

@Serializable
data class Balance(
    val accountId: String,
    val balanceType: BalanceType,
    val value: Double = 0.0
) {
    init {
        if (value < 0) {
            throw RuntimeException("Value need greater then 0")
        }
    }

    fun getAccount(): Account? = AccountRepository.findById(this.accountId)

    fun updateValue(newValue: Double): Balance = this.copy(value = newValue)
}