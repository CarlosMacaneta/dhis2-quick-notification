package org.saudigitus.quicknotification.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.saudigitus.quicknotification.data.model.MessageDetail
import org.saudigitus.quicknotification.data.model.MessagesData
import org.saudigitus.quicknotification.data.remote.MessagingService
import javax.inject.Inject
import timber.log.Timber

class MessagingRepositoryImpl
@Inject constructor(
    private val messagingService: MessagingService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): MessagingRepository {
    override suspend fun getMessageConversationById(
        id: String
    ): MessageDetail? =
       withContext(ioDispatcher) {
           return@withContext messagingService.getMessageConversationById(id).body()
       }

    override suspend fun getMessageConversationByMessageType(
        options: Map<String, String>
    ): MessagesData? =
        withContext(ioDispatcher) {
            return@withContext messagingService.getMessageConversationByMessageType(options).body()
        }

    override suspend fun getMessageConversationByMessageType(
        options: Map<String, String>,
        page: Int,
        pageSize: Int
    ): MessagesData? =
        withContext(ioDispatcher) {
            return@withContext messagingService
                .getMessageConversationByMessageType(
                    options = options,
                    page = page,
                    pageSize = pageSize
                ).body()
        }

    override suspend fun getMessageConversations(): MessagesData?  =
        withContext(ioDispatcher) {
            val result = messagingService.getMessageConversations()

            if (result.isSuccessful) {
                return@withContext result.body()
            } else {
                Timber.e(result.message())
                return@withContext null
            }
        }

    override suspend fun sendMessage(messageId: String, message: String) {
        Timber.tag("MESSAGE_SEN").e("$messageId, $message")
        withContext(ioDispatcher) {
            val result = messagingService.sendMessage(messageId, message)

            Timber.tag("WIOO").e("$result")

            return@withContext result
        }
    }
}