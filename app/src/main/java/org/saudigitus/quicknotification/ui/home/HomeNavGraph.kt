package org.saudigitus.quicknotification.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.saudigitus.quicknotification.data.model.MessageType
import org.saudigitus.quicknotification.ui.conversation.ConversationDetailsScreen
import org.saudigitus.quicknotification.ui.conversation.ConversationRoutes
import org.saudigitus.quicknotification.ui.conversation.ConversationScreen

