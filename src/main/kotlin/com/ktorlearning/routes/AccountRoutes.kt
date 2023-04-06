package com.ktorlearning.routes

import com.ktorlearning.dtos.CreateAccount
import com.ktorlearning.dtos.UpdateBalance
import com.ktorlearning.services.AccountService
import com.ktorlearning.utils.convertToBalanceType
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.accountRouting() {
    route("/accounts") {
        get {
            call.respond(AccountService.findAll())
        }
        get("{accountId}") {
            val accountId = call.parameters["accountId"]!!
            val account = AccountService.findById(accountId)
            account?.let { call.respond(account) } ?: call.respondText(
                "Account $accountId not found",
                status = HttpStatusCode.NotFound
            )
        }
        post {
            val createAccount = call.receive<CreateAccount>()
            call.respond(AccountService.createAccount(createAccount))
        }
        delete("{accountId}") {
            val accountId = call.parameters["accountId"]!!
            AccountService.delete(accountId)
            call.respondText(
                "Account $accountId deleted"
            )
        }
        put("accounts/{accountId}/balances/{balanceType}") {
            val updateBalance = call.receive<UpdateBalance>()
            AccountService.updateBalanceValue(
                call.parameters["accountId"]!!,
                call.parameters["balanceType"]?.convertToBalanceType()!!,
                updateBalance.value
            )
            call.respondText("Balance updated")
        }
    }
}