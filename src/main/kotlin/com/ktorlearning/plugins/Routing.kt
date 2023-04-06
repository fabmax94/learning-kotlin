package com.ktorlearning.plugins

import com.ktorlearning.routes.accountRouting
import com.ktorlearning.routes.transactionRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        accountRouting()
        transactionRouting()
    }
}
