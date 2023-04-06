package com.ktorlearning.services

import com.ktorlearning.dtos.CreateAccount
import com.ktorlearning.dtos.GetAccount
import com.ktorlearning.models.Account
import com.ktorlearning.models.Balance
import com.ktorlearning.models.BalanceType
import com.ktorlearning.repositories.AccountRepository
import com.ktorlearning.repositories.BalanceRepository
import java.util.UUID


object AccountService {
    fun createAccount(createAccount: CreateAccount): GetAccount {
        val account = Account(UUID.randomUUID().toString(), createAccount.name)
        AccountRepository.save(account)
        generateBalances(account).map { BalanceRepository.save(it) }
        return GetAccount(account.id, account.name, listOf())
    }

    fun delete(accountId: String) {
        val account = AccountRepository.findById(accountId)
        account?.let { AccountRepository.remove(account) }
    }

    fun findAll(): Collection<GetAccount> = AccountRepository.findAll()
        .map { account -> GetAccount(account.id, account.name, BalanceRepository.findByAccountId(account.id)) }

    fun findById(accountId: String): GetAccount? {
        val account = AccountRepository.findById(accountId)
        return account?.let {
            GetAccount(
                account.id,
                account.name,
                BalanceRepository.findByAccountId(account.id)
            )
        }
    }

    fun updateBalanceValue(accountId: String, balanceType: BalanceType, value: Double) {
        val balance = BalanceRepository
            .findByAccountIdAndBalanceType(accountId, balanceType)
        val newBalance = balance?.updateValue(value)
        if (newBalance != null) {
            BalanceRepository.update(newBalance)
        }
    }


    private fun generateBalances(account: Account): Collection<Balance> =
        BalanceType.values().map { Balance(account.id, it) }

}



