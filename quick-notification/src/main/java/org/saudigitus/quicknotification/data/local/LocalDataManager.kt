/*
package org.saudigitus.quicknotification.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import org.saudigitus.quicknotification.data.model.MessageConversation
import org.saudigitus.quicknotification.data.model.MessageDetail

@Dao
interface LocalDataManager {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessageDetails(messageDetail: MessageDetail)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessageConversation(messageConversation: MessageConversation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessageConversation(vararg messageConversation: MessageConversation)

    @Query("SELECT * FROM message_details WHERE id = :id")
    suspend fun getMessageConversationById(id: String): MessageDetail?

    @Query("SELECT * FROM message_conversations WHERE messageType = :messageType")
    suspend fun getMessageConversationByMessageType(messageType: String): List<MessageConversation>

    @Update
    fun updateMessageDetails(messageDetail: MessageDetail)

    @Update
    fun updateMessageConversation(messageConversation: MessageConversation)

    @Delete
    fun deleteMessageDetails(messageDetail: MessageDetail)

    @Delete
    fun deleteMessageConversation(messageConversation: MessageConversation)
}*/
