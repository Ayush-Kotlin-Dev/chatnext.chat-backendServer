package com.KtorChat.plugins

import com.KtorChat.room.RoomController
import com.KtorChat.routes.chatSocket
import com.KtorChat.routes.getAllMessages
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {

    val roomController by inject<RoomController>()
    install(Routing){
        chatSocket(roomController)
        getAllMessages(roomController)
        get("/") {
            call.respondText("Hello, KtorChat!")
        }
    }
}
