package org.saudigitus.quicknotification.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext
import org.saudigitus.quicknotification.data.local.MessageDatastoreSerialization
import org.saudigitus.quicknotification.data.local.MessageDetailsDatastoreSerialization
import org.saudigitus.quicknotification.data.model.MessageConversation
import org.saudigitus.quicknotification.data.model.MessageDetail
import org.saudigitus.quicknotification.data.model.MessageType
import java.util.prefs.Preferences
import javax.inject.Inject

val Context.dataStore by dataStore("messages-db.json", MessageDatastoreSerialization)
val Context.messageStore by dataStore(
    "messages-details-db.json", MessageDetailsDatastoreSerialization
)

class LocalDataStoreImpl
@Inject constructor(
    @ApplicationContext val context: Context,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): LocalDataManagerRepository{

    override suspend fun insertMessageDetails(messageDetail: MessageDetail) {
        withContext(ioDispatcher) {
            context.messageStore.updateData {
                val data = it.toMutableList()

                if (data.isNotEmpty() || !data.contains(messageDetail)) {
                    data.add(messageDetail)
                }

                data
            }
        }
    }

    override suspend fun insertMessageConversation(messageConversation: MessageConversation) {
        //TODO("Implement insertion")
    }

    override suspend fun insertMessageConversation(messageConversation: List<MessageConversation>) {
        withContext(ioDispatcher) {
            context.dataStore.updateData {
                val data = it.toMutableList()

                if (data.isNotEmpty() || !data.contains(messageConversation)) {
                    data.add(messageConversation)
                }

                data
            }
        }
    }

    override suspend fun getMessageConversationById(
        id: String
    ): MessageDetail? = withContext(ioDispatcher) {
        return@withContext context.messageStore.data.first().find { it.id == id }
    }

    override suspend fun getMessageConversationByMessageType(
        messageType: MessageType
    ): List<MessageConversation> = withContext(ioDispatcher) {
        return@withContext context.dataStore.data.map { conversations ->
            val result = mutableListOf<MessageConversation>()
            for (messages in conversations) {
                result.addAll(messages.filter { it.messageType == messageType.name })
            }

            result
        }.first()
    }

    override suspend fun getAllMessagesConversation(): Flow<List<List<MessageConversation>>> =
        withContext(ioDispatcher) {
            return@withContext context.dataStore.data
        }

    override suspend fun resetDataStore() {
        context.dataStore.updateData {
            val data = it.toMutableList()
            data.clear()

            data
        }

        context.messageStore.updateData {
            val data = it.toMutableList()
            data.clear()

            data
        }
    }
}