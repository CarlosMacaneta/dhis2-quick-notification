package org.saudigitus.quicknotification.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.dhis2.commons.data.tuples.Trio
import org.dhis2.commons.network.NetworkUtils
import org.dhis2.commons.prefs.PreferenceProvider
import org.dhis2.commons.prefs.SECURE_SERVER_URL
import org.saudigitus.quicknotification.data.model.MessageDetail
import org.saudigitus.quicknotification.data.model.MessageType
import org.saudigitus.quicknotification.data.model.MessagesData
import org.saudigitus.quicknotification.data.remote.Basic64AuthInterceptor
import org.saudigitus.quicknotification.data.remote.EndPoints
import org.saudigitus.quicknotification.data.repository.LocalDataManagerRepository
import org.saudigitus.quicknotification.data.repository.MessagingRepository
import org.saudigitus.quicknotification.ui.conversation.ConversationUiState
import org.saudigitus.quicknotification.ui.conversation.Message
import org.saudigitus.quicknotification.ui.util.Constants.LOGOUT_STATE
import org.saudigitus.quicknotification.ui.util.Constants.PASSWORD
import org.saudigitus.quicknotification.ui.util.Constants.SERVER_URL
import org.saudigitus.quicknotification.ui.util.Constants.USERNAME
import org.saudigitus.quicknotification.ui.util.DateTimeHelper
import org.saudigitus.quicknotification.ui.util.Utils.isOnline
import timber.log.Timber
import java.util.Collections
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val messagingRepository: MessagingRepository,
    private val localRepository: LocalDataManagerRepository,
    private val preferenceProvider: PreferenceProvider,
    private val networkUtils: NetworkUtils
): ViewModel() {

    private val _messageData = MutableStateFlow(MessagesData(null, null))
    val messagesData: StateFlow<MessagesData> = _messageData

    private val _messageDetails = MutableStateFlow(MessageDetail())
    val messageDetails: StateFlow<MessageDetail> = _messageDetails

    private val _conversationUiState = MutableStateFlow<ConversationUiState?>(null)
    val conversationUiState: StateFlow<ConversationUiState?> = _conversationUiState

    private val _conversationId = MutableStateFlow("")
    private val conversationId = _conversationId.asStateFlow()

    private val _messageTypeToLoad = MutableStateFlow(MessageType.PRIVATE)
    private val messageTypeToLoad = _messageTypeToLoad.asStateFlow()

    init {
        Basic64AuthInterceptor.setCredential(
            username = "${preferenceProvider.getString(USERNAME)}",
            password = "${preferenceProvider.getString(PASSWORD)}"
        )

        if (preferenceProvider.getBoolean(LOGOUT_STATE, false)) {
            preferenceProvider.setValue(LOGOUT_STATE, false)
            resetDataStore()
        }

        getMessageConversationByMessageType(messageTypeToLoad.value)
    }

    fun setMessageType(messageType: MessageType) {
        _messageTypeToLoad.value = messageType
        getMessageConversationByMessageType(messageType)
    }

    fun setConversationId(conversationId: String) {
        _conversationId.value = conversationId
        getMessagesByConversationId(conversationId)
    }

    fun refreshData() {
        getMessageConversationByMessageType(messageTypeToLoad.value)
    }

    fun sync(
        onComplete: (message: String, messageCount: Int) -> Unit,
        onCancel: (message: String) -> Unit
    ) {
        if (networkUtils.isOnline()) {
            //resetDataStore()
            viewModelScope.launch {
                var countNewMessages: Int = 0
                val countOldMessages: Int = getTotalMessages()

                for (messageType in MessageType.values()) {
                    val data = messagingRepository.getMessageConversationByMessageType(
                        mapOf(
                            Pair("filter", "messageType:eq:${messageType.name}"),
                            EndPoints.Queries.MESSAGE_CONVERSATION_BY_MESSAGE_TYPE_FIELDS,
                            EndPoints.Queries.ORDER_BY_LAST_MESSAGE
                        )
                    )

                    if (data != null && !data.messageConversations.isNullOrEmpty()) {
                        for (message in data.messageConversations) {
                            if (message != null && message.read == false)
                                countNewMessages ++
                        }
                    }
                }

                preferenceProvider.setValue("MESSAGE_SYNCED", true)
                refreshData()
                onComplete(
                    "Sync completed successfully.",
                    if (countNewMessages == countOldMessages)
                        countOldMessages
                    else if (countNewMessages > countOldMessages)
                        countNewMessages - countOldMessages
                    else
                        countOldMessages
                )
            }
        } else {
            onCancel("You must be connect to internet to sync your messages")
        }
    }

    private fun refreshConversation() {
        getMessagesByConversationId(conversationId.value)
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            messagingRepository.sendMessage(conversationId.value, message)
            //refreshConversation()
        }
    }

    private fun getMessageConversationByMessageType(messageType: MessageType) {
        viewModelScope.launch {
            val message = messagingRepository.getMessageConversationByMessageType(
                mapOf(
                    Pair("filter", "messageType:eq:${messageType.name}"),
                    EndPoints.Queries.MESSAGE_CONVERSATION_BY_MESSAGE_TYPE_FIELDS,
                    EndPoints.Queries.ORDER_BY_LAST_MESSAGE
                )
            )
            if (message != null && networkUtils.isOnline()) {
                if (!preferenceProvider.getBoolean("MESSAGE_SYNCED", false)) {

                    message.messageConversations?.let { msgs ->
                        localRepository.insertMessageConversation(
                            msgs.requireNoNulls()
                        )
                    }
                }

                _messageData.value = message
            } else {
                _messageData.value = MessagesData(
                    pager = null,
                    messageConversations = localRepository
                        .getMessageConversationByMessageType(messageType)
                )
            }
        }
    }

    private fun getMessagesByConversationId(conversationId: String) {
        viewModelScope.launch {
            val conversation = messagingRepository.getMessageConversationById(conversationId)

            if (conversation != null && networkUtils.isOnline()) {
                localRepository.insertMessageDetails(conversation)

                _messageDetails.value = conversation
            } else {
                _messageDetails.value = localRepository.getMessageConversationById(conversationId)!!
            }

            generateBubbleMessage(messageDetails.value)
        }
    }

    private fun generateBubbleMessage(messageDetail: MessageDetail) {
        val messages = messageDetail.messages.map { msg ->
            Message(
                author = "${
                    if (msg?.sender?.displayName != null)
                        msg.sender.displayName
                    else
                        messageDetail.messageType?.replaceFirstChar { first ->
                            if (first.isLowerCase()) first.titlecase(
                                Locale.getDefault()
                            ) else first.toString()
                        }
                }",
                "${msg?.text}",
                DateTimeHelper.getTime(msg?.lastUpdated!!)
            )
        }

        if (messages.isNotEmpty()) {
            _conversationUiState.value = ConversationUiState(
                initialMessages = messages.reversed(),
                channelName = "${messageDetail.subject}",
                channelMembers = messageDetail.userMessages.size
            )
        }
    }

    private suspend fun getTotalMessages(): Int {
        var count: Int = 0

        for (messageType in MessageType.values()) {
            val data = localRepository.getMessageConversationByMessageType(messageType)

            for (message in data) {
                if (message.read == false)
                    count ++
            }
        }

        return count
    }

    private fun resetDataStore() {
        viewModelScope.launch {
            localRepository.resetDataStore()
        }
    }
}