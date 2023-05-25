/*
package org.saudigitus.quicknotification.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.saudigitus.quicknotification.data.local.LocalDataManager
import org.saudigitus.quicknotification.data.model.MessageConversation
import org.saudigitus.quicknotification.data.model.MessageDetail
import org.saudigitus.quicknotification.data.model.MessageType
import javax.inject.Inject

class LocalDataManagerRepositoryImpl
@Inject constructor(
    private val dao: LocalDataManager,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): LocalDataManagerRepository {

    override suspend  fun insertMessageDetails(messageDetail: MessageDetail) {
        withContext(ioDispatcher) {
            dao.insertMessageDetails(messageDetail)
        }
    }

    override suspend  fun insertMessageConversation(messageConversation: MessageConversation) {
        withContext(ioDispatcher) {
            dao.insertMessageConversation(messageConversation)
        }
    }

    override suspend fun insertMessageConversation(vararg messageConversation: MessageConversation) {
        withContext(ioDispatcher) {
            dao.insertMessageConversation(*messageConversation)
        }
    }

    override suspend fun getMessageConversationById(id: String): MessageDetail?
    = withContext(ioDispatcher) {
        return@withContext dao.getMessageConversationById(id)
    }

    override suspend fun getMessageConversationByMessageType(
        messageType: MessageType
    ): List<MessageConversation> = withContext(ioDispatcher) {
        return@withContext dao.getMessageConversationByMessageType(messageType.name)
    }
}*/
