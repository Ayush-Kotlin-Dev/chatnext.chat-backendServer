package com.KtorChat.room

import com.KtorChat.data.MessageDataSource
import com.KtorChat.data.model.Message
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

class RoomController(
    private val messageDataSource: MessageDataSource
) {
    private val members = ConcurrentHashMap<String, Member>()

    fun onJoin(
        userName: String,
        sessionId: String,
        socket: WebSocketSession
    ) {
        if (members.containsKey(userName)) {
            throw MemberAlreadyExistsException()
        }
        members[userName] = Member(
            userName, sessionId, socket
        )

    }

    suspend fun sendMessage(
        senderUsername: String,
        message: String
    ) {
        members.values.forEach { member ->
            val messageEntity = Message(
                userName = senderUsername,
                text = message,
                timestamp = System.currentTimeMillis()

            )
            messageDataSource.insertMessage(messageEntity)

            val parsedMessage = Json.encodeToString(messageEntity)
            member.socket.send(Frame.Text(parsedMessage))
        }
    }

    suspend fun getAllMessages(): List<Message> {
        return messageDataSource.getAllMessages()
    }

    suspend fun tryDisconnect(userName: String) {
        members[userName]?.socket?.close()
        if(members.containsKey(userName)) {
            members.remove(userName)
        }
    }
}
