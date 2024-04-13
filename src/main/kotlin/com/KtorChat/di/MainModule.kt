package com.KtorChat.di

import com.KtorChat.data.MessageDataSource
import com.KtorChat.data.MessageDataSourceImpl
import com.KtorChat.room.RoomController
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo


val mainModule = module{
    single {
        KMongo.createClient()
            .coroutine
            .getDatabase("message_db")
    }
    single<MessageDataSource> {
        MessageDataSourceImpl(get())
    }
    single { RoomController(get()) } // Assuming RoomController takes a dependency in its constructor

}