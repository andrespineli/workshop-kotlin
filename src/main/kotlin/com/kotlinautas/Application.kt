package com.kotlinautas

import com.kotlinautas.database.setupDB
import io.ktor.application.*
import com.kotlinautas.plugins.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    setupDB()
    configureRouting()
    configureSerialization()
    configureHTTP()
}
