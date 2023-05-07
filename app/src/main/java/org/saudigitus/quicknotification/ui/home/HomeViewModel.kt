package org.saudigitus.quicknotification.ui.home

import androidx.compose.ui.text.capitalize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.saudigitus.quicknotification.data.local.PreferenceProvider
import org.saudigitus.quicknotification.data.model.MessageConversation
import org.saudigitus.quicknotification.data.model.MessageDetail
import org.saudigitus.quicknotification.data.model.MessageType
import org.saudigitus.quicknotification.data.model.MessagesData
import org.saudigitus.quicknotification.data.remote.Basic64AuthInterceptor
import org.saudigitus.quicknotification.data.remote.EndPoints
import org.saudigitus.quicknotification.data.remote.MessagingRemoteMediator
import org.saudigitus.quicknotification.data.repository.MessagingRepository
import org.saudigitus.quicknotification.data.source.MessagingSource
import org.saudigitus.quicknotification.ui.conversation.ConversationUiState
import org.saudigitus.quicknotification.ui.conversation.Message
import org.saudigitus.quicknotification.ui.util.Constants.SERVER_URL
import org.saudigitus.quicknotification.ui.util.DateTimeHelper
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val messagingRepository: MessagingRepository,
    private val preferenceProvider: PreferenceProvider
): ViewModel() {

    private val _messageData = MutableStateFlow(MessagesData(null, null))
    val messagesData: StateFlow<MessagesData> = _messageData

    private val _messageDetails = MutableStateFlow(MessageDetail())
    val messageDetails: StateFlow<MessageDetail> = _messageDetails

    private val _conversationUiState = MutableStateFlow<ConversationUiState?>(null)
    val conversationUiState: StateFlow<ConversationUiState?> = _conversationUiState

    private val _conversationId = MutableStateFlow("")
    private val conversationId = _conversationId.asStateFlow()

    init {
        //preferenceProvider.setValue(SERVER_URL, "https://play.dhis2.org/2.38.3.1")

        Basic64AuthInterceptor.setCredential(
            username = "admin",
            password = "district"
        )
    }

    fun setMessageType(messageType: MessageType) {
        getMessageConversationByMessageType(messageType)
    }

    fun setConversationId(conversationId: String) {
        _conversationId.value = conversationId
        getMessagesByConversationId(conversationId)
    }

    private fun refreshConversation() {
        getMessagesByConversationId(conversationId.value)
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            messagingRepository.sendMessage(conversationId.value, message)
            refreshConversation()
        }
    }

    private fun getMessageConversationByMessageType(messageType: MessageType) {
        viewModelScope.launch {
            messagingRepository.getMessageConversationByMessageType(
                mapOf(
                    Pair("filter", "messageType:eq:${messageType.name}"),
                    EndPoints.Queries.MESSAGE_CONVERSATION_BY_MESSAGE_TYPE_FIELDS,
                    EndPoints.Queries.ORDER_BY_LAST_MESSAGE
                )
            )?.let {
                _messageData.value = it
            }
        }
    }

    fun getPagedMessageByMessageType(
        messageType: MessageType
    ): Flow<PagingData<MessageConversation>> {
        fun options() =
            mapOf(
                Pair("filter", "messageType:eq:${messageType.name}"),
                EndPoints.Queries.MESSAGE_CONVERSATION_BY_MESSAGE_TYPE_FIELDS,
                EndPoints.Queries.ORDER_BY_LAST_MESSAGE
            )

        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                MessagingSource(messagingRepository, options())
            }
        ).flow


    }

    private fun getMessagesByConversationId(conversationId: String) {
        viewModelScope.launch {
            messagingRepository.getMessageConversationById(conversationId)?.let {
                _messageDetails.value = it

                val messages = it.messages?.map { msg ->
                    Message(
                        author = "${
                            if (msg?.sender?.displayName != null)
                                msg.sender.displayName
                            else
                                it.messageType?.replaceFirstChar { first ->
                                    if (first.isLowerCase()) first.titlecase(
                                        Locale.getDefault()
                                    ) else first.toString()
                                }
                        }",
                        "${msg?.text}",
                        DateTimeHelper.getTime(msg?.lastUpdated!!)
                    )
                }

                if (messages != null) {
                    _conversationUiState.value = ConversationUiState(
                        initialMessages = messages.reversed(),
                        channelName = "${it.subject}",
                        channelMembers = it.userMessages?.size ?: 0
                    )
                }
            }
        }
    }
}