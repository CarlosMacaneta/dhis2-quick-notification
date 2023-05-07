package org.saudigitus.quicknotification.data.remote

import org.saudigitus.quicknotification.data.model.MessageDetail
import org.saudigitus.quicknotification.data.model.MessagesData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface MessagingService {
    @GET(EndPoints.MESSAGE_CONVERSATION_BY_ID)
    suspend fun getMessageConversationById(@Path("id") id: String): Response<MessageDetail>

    @GET(EndPoints.MESSAGE_CONVERSATIONS_BY_MESSAGE_TYPE)
    suspend fun getMessageConversationByMessageType(
        @QueryMap options: Map<String, String>
    ): Response<MessagesData>

    @GET(EndPoints.MESSAGE_CONVERSATIONS_BY_MESSAGE_TYPE)
    suspend fun getMessageConversationByMessageType(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
        @QueryMap options: Map<String, String>
    ): Response<MessagesData>

    @GET(EndPoints.MESSAGE_CONVERSATIONS)
    suspend fun getMessageConversations(): Response<MessagesData>

    @POST(EndPoints.SEND_MESSAGE_BY_ID)
    suspend fun sendMessage(
        @Path("messageId") messageId: String,
        @Body message: String,
    ): Response<Any>
}