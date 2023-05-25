package org.saudigitus.quicknotification.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
import org.dhis2.commons.sync.OnDismissListener
import org.dhis2.commons.sync.OnSyncNavigationListener
import org.dhis2.commons.sync.SyncContext
import org.dhis2.commons.sync.SyncDialog
import org.dhis2.commons.sync.SyncStatusItem
import org.saudigitus.quicknotification.R
import org.saudigitus.quicknotification.data.model.MessageType
import org.saudigitus.quicknotification.ui.home.HomeDrawer
import org.saudigitus.quicknotification.ui.home.HomeNavGraph
import org.saudigitus.quicknotification.ui.home.HomeNavigation
import org.saudigitus.quicknotification.ui.home.HomeRoutes
import org.saudigitus.quicknotification.ui.home.HomeViewModel
import org.saudigitus.quicknotification.ui.theme.QuickNotificationTheme
import org.saudigitus.quicknotification.ui.util.NotificationHelper
import org.saudigitus.quicknotification.ui.util.Utils.setProgramTheme

@AndroidEntryPoint
class QuickNotificationActivity : ComponentActivity() {

    private val viewModel: HomeViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            setProgramTheme(this, R.style.AppTheme)

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
                gesturesEnabled = false
            ) {
                NavHost(
                    navController = navController,
                    startDestination = selectedDestination,
                ) {
                    composable(HomeRoutes.INBOX) {
                        viewModel.setMessageType(MessageType.PRIVATE)
                        HomeNavGraph(viewModel, MessageType.PRIVATE, drawerState, sync = { sync() })
                    }
                    composable(HomeRoutes.VALIDATION) {
                        viewModel.setMessageType(MessageType.VALIDATION_RESULT)
                        HomeNavGraph(viewModel, MessageType.VALIDATION_RESULT, drawerState, sync = { sync() })
                    }
                    composable(HomeRoutes.TICKET) {
                        viewModel.setMessageType(MessageType.TICKET)
                        HomeNavGraph(viewModel, MessageType.TICKET, drawerState, sync = { sync() })
                    }
                    composable(HomeRoutes.SYSTEM) {
                        viewModel.setMessageType(MessageType.SYSTEM)
                        HomeNavGraph(viewModel, MessageType.SYSTEM, drawerState, sync = { sync() })
                    }
                    composable(HomeRoutes.MAIN) {
                        //startActivity(Intent(this, ))
                        finish()
                    }
                }
            }
        }
    }

    private fun sync() {
        Toast.makeText(this, "Sync has started", Toast.LENGTH_SHORT).show()
        viewModel.sync(
            onComplete = { message, messageCount ->
                NotificationHelper.triggerNotification(
                    this,
                    710776,
                    "MESSAGE_DATA",
                    "MESSAGE_DATA_CHANNEL",
                    "$messageCount messages",
                    message,
                    R.drawable.ic_mark_email_unread
                )
            },
            onCancel = {message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                NotificationHelper.cancelNotification(this, 710776)
            }
        )
    }
}