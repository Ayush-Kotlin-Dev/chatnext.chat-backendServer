package com.KtorChat.routes

import com.KtorChat.room.MemberAlreadyExistsException
import com.KtorChat.room.RoomController
import com.KtorChat.sessions.ChatSession
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach

fun Route.chatSocket(roomController: RoomController) {
    webSocket("/chat-socket") {
        val session = call.sessions.get<ChatSession>()
        if (session == null) {
            close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No session"))
            return@webSocket
        }
        try {
            roomController.onJoin(
                userName = session.userName,
                socket = this,
                sessionId = session.sessionId
            )
            incoming.consumeEach { frame ->
                if (frame is Frame.Text) {
                    roomController.sendMessage(session.userName, frame.readText())

                }
            }
        } catch (e: MemberAlreadyExistsException) {

            call.respond(HttpStatusCode.Conflict)
        } catch (e: Exception) {
            e.printStackTrace()
        }finally {
            roomController.tryDisconnect(session.userName)
        }


    }
}

fun Route.getAllMessages(roomController: RoomController) {
    get("/messages") {
        call.respond(
            HttpStatusCode.OK,
            roomController.getAllMessages()
        )
    }
}