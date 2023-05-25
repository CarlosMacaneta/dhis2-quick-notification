package org.saudigitus.quicknotification.data.repository

import kotlinx.coroutines.flow.Flow
import org.saudigitus.quicknotification.data.model.MessageConversation
import org.saudigitus.quicknotification.data.model.MessageDetail
import org.saudigitus.quicknotification.data.model.MessageType

interface LocalDataManagerRepository {

    suspend fun insertMessageDetails(messageDetail: MessageDetail)
    suspend fun insertMessageConversation(messageConversation: MessageConversation)
    suspend fun insertMessageConversation(messageConversation: List<MessageConversation>)
    suspend fun getMessageConversationById(id: String): MessageDetail?
    suspend fun getMessageConversationByMessageType(messageType: MessageType): List<MessageConversation>
    suspend fun getAllMessagesConversation(): Flow<List<List<MessageConversation>>>
    suspend fun resetDataStore()
}