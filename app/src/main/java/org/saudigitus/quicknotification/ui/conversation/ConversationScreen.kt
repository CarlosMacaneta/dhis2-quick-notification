/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.saudigitus.quicknotification.ui.conversation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.saudigitus.quicknotification.data.model.MessageType
import org.saudigitus.quicknotification.ui.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationScreen(
    viewModel: HomeViewModel,
    messageType: MessageType,
    dismissChat: () -> Unit
) {
    val configuration = LocalConfiguration.current

    val uiState by viewModel.conversationUiState.collectAsState()

    val navController = rememberNavController()
    val navigationActions = remember(navController) {
        ConversationNavigationAction(navController)
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute =
        navBackStackEntry?.destination?.route ?: ConversationRoutes.CONVERSATION

    Surface(
        modifier = Modifier.fillMaxWidth()
            .height(configuration.screenHeightDp.dp)
    ) {
        NavHost(
            navController = navController,
            startDestination = currentRoute
        ) {
            composable(route = ConversationRoutes.CONVERSATION) {
                uiState?.let {
                    ConversationContent(
                        viewModel,
                        uiState = it,
                        navigateToProfile = { _ -> },
                        onNavBackPressed = dismissChat,
                        onNavInfoPressed = navigationActions.navigateToChatDetails
                    )
                }
            }
            composable(route = ConversationRoutes.CONVERSATION_DETAILS) {
                ConversationDetailsScreen(viewModel, messageType, navigationActions.navigateToChat)
                BackHandler(true) {
                    navigationActions.navigateToChat
                }
            }
        }
    }

}
