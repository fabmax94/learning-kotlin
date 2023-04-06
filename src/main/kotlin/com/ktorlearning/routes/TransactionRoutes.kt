package com.ktorlearning.routes

import com.ktorlearning.dtos.CreateTransaction
import com.ktorlearning.services.TransactionService
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.transactionRouting() {
    route("/transactions") {
        post("process") {
            val createTransaction = call.receive<CreateTransaction>()
            call.respondText(TransactionService.process(createTransaction))
        }
    }
}