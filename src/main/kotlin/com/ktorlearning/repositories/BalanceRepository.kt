package com.ktorlearning.repositories

import com.ktorlearning.models.Balance
import com.ktorlearning.models.BalanceType


object BalanceRepository : Repository<Balance>() {
    override fun equal(entityA: Balance, entity: Balance): Boolean =
        entity.accountId == entityA.accountId && entity.balanceType == entityA.balanceType

    fun findByAccountIdAndBalanceType(
        accountId: String,
        balanceType: BalanceType
    ): Balance? {
        val balance =
            this.entities.find { this.equal(it, Balance(accountId, balanceType)) }

        return when (balance) {
            null -> this.entities.find {
                it.accountId == accountId && it.balanceType == BalanceType.CASH
            }

            else -> balance
        }
    }

    fun findAll(): Collection<Balance> = this.entities

    fun findByAccountId(accountId: String): Collection<Balance> = this.entities.filter { it.accountId == accountId }
}