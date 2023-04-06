package com.ktorlearning.services

import com.ktorlearning.dtos.CreateTransaction
import com.ktorlearning.models.Account
import com.ktorlearning.models.Balance
import com.ktorlearning.models.BalanceType
import com.ktorlearning.repositories.AccountRepository
import com.ktorlearning.repositories.BalanceRepository
import com.ktorlearning.utils.convertToBalanceType
import io.ktor.server.plugins.*

object TransactionService {
    private const val CASH_TAX: Double = 0.12
    private const val DEFAULT_TAX: Double = 0.0

    private val TRANSACTION_STATUS = mapOf(
        "INSUFFICIENT_BALANCE" to "Insufficient Balance",
        "INVALID_DATA" to "Invalid Data",
        "OK" to "OK"
    )

    data class DataTransaction(
        val sourceAccount: Account,
        val destinationAccount: Account,
        val sourceBalance: Balance,
        val destinationBalance: Balance
    )

    private fun validateTransaction(value: Double, data: DataTransaction?): String? = when {
        data == null -> TRANSACTION_STATUS["INVALID_DATA"]
        data.sourceBalance.value < value -> TRANSACTION_STATUS["INSUFFICIENT_BALANCE"]
        else -> null
    }

    private fun findDataToTransaction(
        transaction: CreateTransaction,
        balanceType: BalanceType
    ): DataTransaction? = try {
        DataTransaction(
            AccountRepository.findById(transaction.sourceAccountId)
                ?: throw NotFoundException("Source account not found"),
            AccountRepository.findById(transaction.destinationAccountId)
                ?: throw NotFoundException("Source account not found"),
            BalanceRepository.findByAccountIdAndBalanceType(transaction.sourceAccountId, balanceType)
                ?: throw NotFoundException("Balance ${transaction.sourceAccountId} $balanceType not found"),
            BalanceRepository.findByAccountIdAndBalanceType(transaction.destinationAccountId, balanceType)
                ?: throw NotFoundException("Balance ${transaction.destinationAccountId} $balanceType not found")
        )
    } catch (e: NotFoundException) {
        println(e)
        null
    }


    private fun createExecuteTransactionFunction(
        balanceType: BalanceType
    ): (Balance, Balance, Double) -> Unit {
        val tax: Double = when (balanceType) {
            BalanceType.CASH -> CASH_TAX
            else -> DEFAULT_TAX
        }


        return { sourceBalance: Balance, destinationBalance: Balance, value: Double ->
            val valueTax = value - (value * tax)

            AccountService.updateBalanceValue(
                sourceBalance.accountId,
                balanceType,
                sourceBalance.value - value
            )

            AccountService.updateBalanceValue(
                destinationBalance.accountId,
                balanceType,
                destinationBalance.value + valueTax
            )

        }
    }

    private fun executeProcess(
        value: Double,
        data: DataTransaction?,
        balanceType: BalanceType
    ): String {
        return when (val validateStatus = validateTransaction(value, data)) {
            null -> {
                if (data != null) {
                    val executeTransactionFunction = createExecuteTransactionFunction(
                        balanceType
                    )

                    executeTransactionFunction(
                        data.sourceBalance,
                        data.destinationBalance,
                        value
                    )
                }
                return TRANSACTION_STATUS.getValue("OK")
            }

            else -> validateStatus
        }
    }

    fun process(transaction: CreateTransaction): String {
        val balanceType = transaction.balanceType.convertToBalanceType()
        val data = this.findDataToTransaction(transaction, balanceType)

        return executeProcess(transaction.value, data, balanceType)
    }
}

