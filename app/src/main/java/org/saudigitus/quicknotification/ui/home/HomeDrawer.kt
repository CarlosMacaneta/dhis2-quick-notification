package org.saudigitus.quicknotification.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeDrawer(
    modifier: Modifier = Modifier,
    currentItem: String,
    badge: String? = null,
    onClick: (HomeDestination) -> Unit,
    closeDrawer: () -> Unit
) {
    ModalDrawerSheet(
        modifier = modifier,
        drawerShape = RoundedCornerShape(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.Top),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Quick Notification",
                fontSize = 24.sp,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.primary
            )
            Column {
                HOME_DESTINATIONS.forEach {
                    NavigationDrawerItem(
                        label = {
                            Text(text = it.label)
                        },
                        icon = {
                            it.icon?.let { icon ->
                                Icon(imageVector = icon, contentDescription = it.label)
                            }
                        },
                        badge = {
                            it.badge?.let { badge ->
                                Text(text = badge.ifEmpty { "0" })
                            }
                        },
                        selected = currentItem == it.route,
                        onClick = {
                            onClick(it)
                            closeDrawer()
                        }
                    )
                }
            }
        }
    }
}