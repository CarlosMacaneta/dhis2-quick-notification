package org.saudigitus.quicknotification.ui.conversation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

internal object ConversationRoutes {
    const val CONVERSATION = "conversation"
    const val CONVERSATION_DETAILS = "conversations_details"
}

class ConversationNavigationAction(navController: NavHostController) {
    val navigateToChat: () -> Unit = {
        navController.navigate(ConversationRoutes.CONVERSATION) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToChatDetails: () -> Unit = {
        navController.navigate(ConversationRoutes.CONVERSATION_DETAILS) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}