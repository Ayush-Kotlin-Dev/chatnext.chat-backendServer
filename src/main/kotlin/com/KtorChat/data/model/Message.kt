package com.KtorChat.data.model

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Message (
    val text : String,
    val userName : String,
    val timestamp : Long,
    @BsonId
    val id : String = ObjectId().toString()
)