package org.saudigitus.quicknotification.ui.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.saudigitus.quicknotification.R
import org.saudigitus.quicknotification.data.model.MessageType
import org.saudigitus.quicknotification.ui.conversation.ConversationContent
import org.saudigitus.quicknotification.ui.conversation.ConversationDetailsScreen
import org.saudigitus.quicknotification.ui.conversation.ConversationNavigationAction
import org.saudigitus.quicknotification.ui.conversation.ConversationRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeNavGraph(
    viewModel: HomeViewModel,
    messageType: MessageType,
    drawerState: DrawerState,
    sync: () -> Unit
) {
    val configuration = LocalConfiguration.current

    val uiState by viewModel.conversationUiState.collectAsState()

    val navController = rememberNavController()
    val navActions = remember(navController) {
        ConversationNavigationAction(navController)
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedDestination =
        navBackStackEntry?.destination?.route ?: ConversationRoutes.CHAT_LIST

    NavHost(
        navController = navController,
        startDestination = ConversationRoutes.CHAT_LIST
    ) {
        composable(ConversationRoutes.CHAT_LIST) {
            HomeScreen(viewModel, drawerState, messageType, sync = { sync() }) {
                viewModel.setConversationId(it)
                navController.navigate(ConversationRoutes.CONVERSATION)
            }
        }
        composable(ConversationRoutes.CONVERSATION) {
            if (uiState != null) {
                ConversationContent(
                    viewModel,
                    uiState = uiState!!,
                    navigateToProfile = { _ -> },
                    onNavBackPressed = {
                        navController.popBackStack(
                            route = ConversationRoutes.CHAT_LIST,
                            inclusive = false
                        )
                    },
                    onNavInfoPressed = {
                        navController.navigate(ConversationRoutes.CONVERSATION_DETAILS)
                    }
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier.size(192.dp),
                        painter = painterResource(R.drawable.ic_no_message),
                        contentDescription = stringResource(R.string.no_messages)
                    )
                }
            }
        }
        composable(ConversationRoutes.CONVERSATION_DETAILS) {
            ConversationDetailsScreen(viewModel, messageType) {
                navController.popBackStack(route = ConversationRoutes.CONVERSATION, inclusive = false)
            }
        }
    }

}
