package org.saudigitus.quicknotification.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.saudigitus.quicknotification.data.model.MessageType
import org.saudigitus.quicknotification.ui.home.HomeDrawer
import org.saudigitus.quicknotification.ui.home.HomeNavigation
import org.saudigitus.quicknotification.ui.home.HomeRoutes
import org.saudigitus.quicknotification.ui.home.HomeScreen
import org.saudigitus.quicknotification.ui.home.HomeViewModel
import org.saudigitus.quicknotification.ui.theme.QuickNotificationTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: HomeViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            QuickNotificationTheme(
                darkTheme = false,
                dynamicColor = false
            ) {
                val navController = rememberNavController()
                val navigationActions = remember(navController) {
                    HomeNavigation(navController)
                }
                val coroutineScope = rememberCoroutineScope()

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val selectedDestination =
                    navBackStackEntry?.destination?.route ?: HomeRoutes.INBOX

                val drawerState = rememberDrawerState(DrawerValue.Closed)

                BackHandler {
                    coroutineScope.launch {
                        drawerState.close()
                    }
                }

                ModalNavigationDrawer(
                    drawerContent = {
                        HomeDrawer(
                            currentItem = selectedDestination,
                            onClick = navigationActions::navigateTo,
                            closeDrawer = {
                                coroutineScope.launch {
                                    drawerState.close()
                                }
                            }
                        )
                    },
                    drawerState = drawerState,
                    gesturesEnabled = true
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = HomeRoutes.INBOX,
                    ) {
                        composable(HomeRoutes.INBOX) {
                            viewModel.setMessageType(MessageType.PRIVATE)
                            HomeScreen(viewModel, drawerState, MessageType.PRIVATE) {
                                viewModel.setConversationId(it)
                            }
                        }
                        composable(HomeRoutes.VALIDATION) {
                            viewModel.setMessageType(MessageType.VALIDATION_RESULT)
                            HomeScreen(viewModel, drawerState, MessageType.VALIDATION_RESULT) {
                                viewModel.setConversationId(it)
                            }
                        }
                        composable(HomeRoutes.TICKET) {
                            viewModel.setMessageType(MessageType.TICKET)
                            HomeScreen(viewModel, drawerState, MessageType.TICKET) {
                                viewModel.setConversationId(it)
                            }
                        }
                        composable(HomeRoutes.SYSTEM) {
                            viewModel.setMessageType(MessageType.SYSTEM)
                            HomeScreen(viewModel, drawerState, MessageType.SYSTEM) {
                                viewModel.setConversationId(it)
                            }
                        }
                    }
                }
            }
        }
    }
}