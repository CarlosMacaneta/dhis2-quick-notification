package org.saudigitus.quicknotification.data.remote

object EndPoints {

    const val DEV_BASE_URL = "https://play.dhis2.org/2.38.3.1"

    const val MESSAGE_CONVERSATION_BY_ID = "messageConversations/{id}.json?fields=id,read," +
        "followup,lastMessage,messageType,priority,subject,status,favorite," +
        "assignee[id, displayName],messages[id,lastUpdated,text,sender[id,displayName]," +
        "attachments[id,name,contentLength]],userMessages[user[id, displayName]]"

    const val MESSAGE_CONVERSATIONS_BY_MESSAGE_TYPE = "messageConversations.json"

    const val MESSAGE_CONVERSATIONS = "messageConversations.json?fields=id,subject,displayName," +
        "followUp,lastUpdated,userMessages[user[id, displayName],messages[id,sender[displayName," +
        "gender],text,lastUpdated]"

    const val SEND_MESSAGE_BY_ID = "messageConversations/{messageId}?internal=false"

    object Queries {
        val MESSAGE_CONVERSATION_BY_MESSAGE_TYPE_FIELDS = Pair(
            "fields",
            "id,subject,messageType,lastSender[id,displayName],assignee[id, displayName],status," +
                "priority,lastUpdated,read,lastMessage,followUp,messageCount"
        )

        val ORDER_BY_LAST_MESSAGE = Pair("order", "lastMessage:desc")
    }
}