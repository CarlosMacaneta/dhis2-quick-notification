package org.saudigitus.quicknotification.ui.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material.icons.filled.Verified
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import org.saudigitus.quicknotification.ui.conversation.ConversationRoutes

internal object HomeRoutes {
    const val INBOX = "inbox"
    const val VALIDATION = "validation"
    const val TICKET = "ticket"
    const val SYSTEM = "system"
}

data class HomeDestination(
    val label: String,
    val icon: ImageVector? = null,
    val badge: String? = null,
    val route: String
)

class HomeNavigation(
    private val navController: NavHostController
) {
    fun navigateTo(destination: HomeDestination) {
        navController.navigate(destination.route) {

            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }

            launchSingleTop = true

            restoreState = true
        }
    }
}

val HOME_DESTINATIONS = listOf(
    HomeDestination(
        label = "Inbox",
        icon = Icons.Default.Inbox,
        badge = "3",
        route = HomeRoutes.INBOX
    ),
    HomeDestination(
        label = "Validation",
        icon = Icons.Default.Verified,
        badge = "11",
        route = HomeRoutes.VALIDATION
    ),
    HomeDestination(
        label = "Ticket",
        icon = Icons.Default.FactCheck,
        badge = "4",
        route = HomeRoutes.TICKET
    ),
    HomeDestination(
        label = "System",
        icon = Icons.Default.SystemUpdate,
        badge = "99+",
        route = HomeRoutes.SYSTEM
    )
)