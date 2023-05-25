package org.saudigitus.quicknotification.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.saudigitus.quicknotification.R

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
        drawerShape = RoundedCornerShape(0.dp),
        drawerContainerColor = Color.White
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
                color = Color(colorResource(id = R.color.colorPrimary).toArgb())
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
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = Color(0xFFCAE5FB),
                            unselectedContainerColor = Color.White
                        )
                    )
                }
            }
            Divider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = Color.LightGray.copy(.25f)
            )
            NavigationDrawerItem(
                label = {
                    Text(text = stringResource(id = R.string.menu_home))
                },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = stringResource(id = R.string.menu_home)
                    )
                },
                selected = false,
                onClick = {
                    onClick(HomeDestination())
                    closeDrawer()
                },
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = Color(0xFFCAE5FB),
                    unselectedContainerColor = Color.White
                )
            )
        }
    }
}