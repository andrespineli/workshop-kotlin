package com.kotlinautas.plugins

import com.kotlinautas.controllers.userRoute
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.application.*
import io.ktor.response.*

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        get("/number") {
            call.respond(HttpStatusCode.Created, 42)
        }

        userRoute()
    }
}
