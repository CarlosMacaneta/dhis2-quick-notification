/*
package org.saudigitus.quicknotification.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import org.saudigitus.quicknotification.data.model.MessageConversation
import org.saudigitus.quicknotification.data.model.MessageDetail
import org.saudigitus.quicknotification.data.model.UserMessage


@Database(
    entities = [
        MessageConversation::class,
        MessageDetail::class,
        UserMessage::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun messagesDao(): LocalDataManager

    companion object {
        const val DB_NAME = "messages_db"
    }
}*/
