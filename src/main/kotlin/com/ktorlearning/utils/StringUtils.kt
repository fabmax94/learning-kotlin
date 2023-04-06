package com.ktorlearning.utils

import com.ktorlearning.models.BalanceType

fun String.convertToBalanceType() = when (this) {
    "1", "2" -> BalanceType.FOOD
    "3", "4" -> BalanceType.MEAL
    else -> BalanceType.CASH
}