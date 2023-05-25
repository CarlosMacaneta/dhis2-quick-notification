package org.saudigitus.quicknotification.data.local

import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import org.saudigitus.quicknotification.data.model.MessageConversation
import org.saudigitus.quicknotification.data.model.MessageDetail
import timber.log.Timber
import java.io.InputStream
import java.io.OutputStream

object MessageDatastoreSerialization : Serializer<List<List<MessageConversation>>> {
    override val defaultValue: List<List<MessageConversation>>
        get() = emptyList()

    override suspend fun readFrom(input: InputStream): List<List<MessageConversation>> {
        return try {
            Json.decodeFromString(
                deserializer = ListSerializer(ListSerializer(MessageConversation.serializer())),
                string = input.readBytes().decodeToString()
            )
        } catch (e: java.lang.Exception) {
            Timber.e(e)

            defaultValue
        }
    }

    override suspend fun writeTo(t: List<List<MessageConversation>>, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(
                    serializer = ListSerializer(ListSerializer(MessageConversation.serializer())),
                    value = t
                ).encodeToByteArray()
            )
        }
    }
}

object MessageDetailsDatastoreSerialization : Serializer<List<MessageDetail>> {
    override val defaultValue: List<MessageDetail>
        get() = emptyList()

    override suspend fun readFrom(input: InputStream): List<MessageDetail> {
        return try {
            Json.decodeFromString(
                deserializer = ListSerializer(MessageDetail.serializer()),
                string = input.readBytes().decodeToString()
            )
        } catch (e: java.lang.Exception) {
            Timber.e(e)

            defaultValue
        }
    }

    override suspend fun writeTo(t: List<MessageDetail>, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(
                    serializer = ListSerializer(MessageDetail.serializer()),
                    value = t
                ).encodeToByteArray()
            )
        }
    }
}