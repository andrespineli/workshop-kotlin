package com.kotlinautas.schemas

import com.kotlinautas.models.User
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object Users : Table() {
    val id: Column<String> = char("id", 36)
    val name: Column<String> = varchar("name", 50)
    val password: Column<String> = varchar("password", 50)
    override val primaryKey = PrimaryKey(id, name = "PK_User_Id")

    fun toUser(row: ResultRow) = User(id = row[id], name = row[name], password = row[password])
}