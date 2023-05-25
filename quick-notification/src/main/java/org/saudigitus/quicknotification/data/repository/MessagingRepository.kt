package org.saudigitus.quicknotification.data.repository

import okhttp3.RequestBody
import org.saudigitus.quicknotification.data.model.MessageConversation
import org.saudigitus.quicknotification.data.model.MessageDetail
import org.saudigitus.quicknotification.data.model.MessagesData
import org.saudigitus.quicknotification.ui.conversation.Message

interface MessagingRepository {

    suspend fun getMessageConversationById(id: String): MessageDetail?

    suspend fun getMessageConversationByMessageType(options: Map<String, String>): MessagesData?
    suspend fun getMessageConversationByMessageType(
        options: Map<String, String>,
        page: Int,
        pageSize: Int
    ): MessagesData?

    suspend fun getMessageConversations(): MessagesData?

    suspend fun sendMessage(messageId: String, message: String)
}