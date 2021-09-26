package com.kotlinautas.controllers

import com.kotlinautas.models.User
import com.kotlinautas.schemas.Users
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.userRoute() {
    route("/users") {
        getUsers()
        getUser()
        createUser()
        updateUser()
        removeUser()
    }
}

private fun Route.getUsers() {
    get {
        val users = transaction {
            Users.selectAll().map { Users.toUser(it) }
        }

        return@get call.respond(HttpStatusCode.OK, users)
    }
}

private fun Route.getUser() {
    get("{id}") {
        val id = call.parameters["id"] ?: return@get call.respondText(
            "Id must be provided",
            status = HttpStatusCode.BadRequest
        )

        val user = transaction {
            Users.select { Users.id eq id }.firstOrNull()
        } ?: return@get call.respondText("Not found user", status = HttpStatusCode.NotFound)

        return@get call.respond(Users.toUser(user))
    }
}

private fun Route.createUser() {
    post {
        val user = call.receive<User>()

        val insertion = transaction {
            Users.insert {
                it[id] = user.id
                it[name] = user.name
                it[password] = user.password
            }
        }

        if (insertion.equals(0)) {
            return@post call.respondText("User creation failed", status = HttpStatusCode.BadRequest)
        }

        return@post call.respond(HttpStatusCode.Created, user)
    }
}

private fun Route.updateUser() {
    put("{id}") {
        val id = call.parameters["id"] ?: return@put call.respondText(
            "Id must be provided",
            status = HttpStatusCode.BadRequest
        )

        val user = call.receive<User>()

        val update = transaction {
            Users.update({ Users.id eq id }) {
                it[name] = user.name
                it[password] = user.password
            }
        }

        if (update.equals(0)) {
            return@put call.respondText("Error on update user", status = HttpStatusCode.BadRequest)
        }

        return@put call.respond(User(id, user.name, user.password))
    }
}

private fun Route.removeUser() {
    delete("{id}") {
        val id = call.parameters["id"] ?: return@delete call.respondText(
            "Id must be provided",
            status = HttpStatusCode.BadRequest
        )

        val remove = transaction {
            Users.deleteWhere { Users.id eq id }
        }

        if (remove.equals(0)) {
            return@delete call.respondText("Error on delete user", status = HttpStatusCode.BadRequest)
        }

        return@delete call.respondText("Deleted user", status = HttpStatusCode.OK)
    }
}