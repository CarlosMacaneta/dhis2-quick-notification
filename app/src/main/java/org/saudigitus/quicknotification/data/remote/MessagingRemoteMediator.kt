package org.saudigitus.quicknotification.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import org.saudigitus.quicknotification.data.model.MessageConversation
import org.saudigitus.quicknotification.data.repository.MessagingRepository
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class MessagingRemoteMediator(
    private val repository: MessagingRepository,
    private val options: Map<String, String>
): RemoteMediator<Int, MessageConversation>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MessageConversation>
    ): MediatorResult {
        return try {
            val loadKey = when(loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if(lastItem == null) {
                        1
                    } else {
                        (lastItem.messageCount?.div(state.config.pageSize))?.plus(1)
                    }
                }
            }

            val conversations = loadKey?.let {
                repository.getMessageConversationByMessageType(
                    options = options,
                    page = it,
                    pageSize = state.config.pageSize
                )
            }

            MediatorResult.Success(
                endOfPaginationReached = conversations?.messageConversations?.isEmpty() == true
            )
        } catch(e: IOException) {
            MediatorResult.Error(e)
        } catch(e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}