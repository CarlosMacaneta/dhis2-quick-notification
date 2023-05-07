package org.saudigitus.quicknotification.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import org.saudigitus.quicknotification.data.model.MessageConversation
import org.saudigitus.quicknotification.data.model.MessagesData
import org.saudigitus.quicknotification.data.repository.MessagingRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MessagingSource
@Inject constructor(
    private val messageRepository: MessagingRepository,
    private val options: Map<String, String>
) : PagingSource<Int, MessageConversation>() {

    override fun getRefreshKey(state: PagingState<Int, MessageConversation>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MessageConversation> {
        return try {
            val nextPage = params.key ?: 1

            val data = messageRepository.getMessageConversationByMessageType(
                mapOf(),
                nextPage,
                20
            )

            val conversations = data?.messageConversations?.requireNoNulls() ?: listOf()

            LoadResult.Page(
                data = conversations,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (data == null) null else data.pager?.page?.plus(1)
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}